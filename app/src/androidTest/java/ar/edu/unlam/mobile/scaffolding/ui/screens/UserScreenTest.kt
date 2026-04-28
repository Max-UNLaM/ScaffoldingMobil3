package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Functional interface used as a Mockito-friendly wrapper around the [onError] lambda.
 * Mockito cannot mock plain Kotlin function types (e.g. `(Exception) -> Unit`) directly,
 * but it can mock single-abstract-method (SAM) interfaces.
 */
fun interface OnErrorCallback {
    fun onError(exception: Exception)
}

/**
 * Instrumented Compose + Hilt tests for [UserScreen].
 *
 * WHY Hilt here?
 *   [UserScreen] injects [UserViewModel] via [dagger.hilt.android.lifecycle.HiltViewModel].
 *   The Compose test rule needs a real Activity to host the ViewModel store, and that
 *   Activity must participate in the Hilt component hierarchy.
 *   [HiltTestActivity] is a minimal Activity provided by hilt-android-testing for exactly
 *   this purpose — it avoids pulling in the entire [ar.edu.unlam.mobile.scaffolding.MainActivity].
 *
 * Rule ordering matters:
 *   [HiltAndroidRule] (order = 0) must run BEFORE [createAndroidComposeRule] (order = 1)
 *   so that the Hilt component is ready when the Activity is launched.
 *
 * WHY Mockito here?
 *   [UserScreen] accepts an [onError] lambda that is called when [UserViewModel] transitions
 *   to an error state. We cannot inspect that callback directly from the outside.
 *   By wrapping it in a [OnErrorCallback] functional interface we can:
 *     - Use Mockito to create a mock implementation.
 *     - Call [verify] after the error state is reached to assert it was invoked.
 *     - Use [ArgumentCaptor] to inspect the [Exception] that was passed.
 */

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    // createAndroidComposeRule launches HiltTestActivity, which is declared in
    // app/src/debug/AndroidManifest.xml and supports Hilt injection.
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    // -------------------------------------------------------------------------
    // Scenario: two loading indicators are shown immediately after launch
    // -------------------------------------------------------------------------
    // Given: a freshly started UserScreen (t = 0)
    // When:  the UI renders before any ViewModel delay has elapsed
    // Then:  two CircularProgressIndicators are visible — one for each state section
    @Test
    fun `given UserScreen just launched, two loading indicators are visible`() {
        hiltRule.inject()

        composeTestRule.setContent {
            UserScreen(userId = "test-user", onError = {})
        }

        // Both userNameState and textListState start as Loading.
        // CircularProgressIndicator has ProgressBarRangeInfo.Indeterminate semantics.
        composeTestRule
            .onAllNodes(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertCountEquals(2)
    }

    // -------------------------------------------------------------------------
    // Scenario: onError callback is invoked after the ViewModel error transition
    // -------------------------------------------------------------------------
    // Given: a UserScreen with a Mockito-mocked OnErrorCallback
    // When:  4 000 ms of real time elapse and textListState transitions to Error
    // Then:  the mock's onError method was called once with an Exception whose
    //        message is "se rompio todo"
    //
    // NOTE: This test waits up to 6 seconds because the ViewModel uses real delays
    //       in viewModelScope.launch. In a production app you would inject the
    //       dispatcher to make it testable without real waiting.
    @Test
    fun `given UserScreen after 4s, onError is called with the error message`() {
        hiltRule.inject()

        // Mockito creates a mock of our SAM interface — calls are recorded automatically.
        val mockCallback = mock<OnErrorCallback>()

        composeTestRule.setContent {
            UserScreen(
                userId = "test-user",
                // Bridge the lambda to our mockable interface
                onError = { e -> mockCallback.onError(e) },
            )
        }

        // Poll until the mock receives the call or the timeout expires.
        // waitUntil re-evaluates the lambda on the UI thread until it returns true.
        composeTestRule.waitUntil(timeoutMillis = 6_000) {
            try {
                verify(mockCallback).onError(any())
                true
            } catch (_: AssertionError) {
                // Not called yet — keep waiting
                false
            }
        }

        // Authoritative verification: capture the exact exception that was passed.
        val captor = ArgumentCaptor.forClass(Exception::class.java)
        verify(mockCallback).onError(captor.capture())
        assertEquals("se rompio todo", captor.value.message)
    }
}
