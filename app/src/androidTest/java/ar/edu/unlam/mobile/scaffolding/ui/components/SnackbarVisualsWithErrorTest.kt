package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Instrumented tests for [SnackbarVisualsWithError].
 *
 * This file demonstrates two complementary techniques:
 *
 * 1. Plain assertions — verifying the real class behaviour directly.
 * 2. Mockito mocks   — creating a fake [SnackbarVisuals] to contrast against the
 *    real implementation. This pattern is useful when:
 *      - You want to isolate the class under test from its collaborators.
 *      - You need to verify that a dependency was called with specific arguments.
 *
 * These tests run on device/emulator because [SnackbarDuration] lives in the
 * androidx.compose.material3 library, which requires the Android runtime.
 */
@RunWith(AndroidJUnit4::class)
class SnackbarVisualsWithErrorTest {
    // -------------------------------------------------------------------------
    // Scenario: error snackbar displays "Error" as the action label
    // -------------------------------------------------------------------------
    // Given: a SnackbarVisualsWithError where isError = true
    // When:  actionLabel is read
    // Then:  it returns "Error"
    @Test
    fun `given isError is true, actionLabel is Error`() {
        val visuals = SnackbarVisualsWithError(message = "Something went wrong", isError = true)
        assertEquals("Error", visuals.actionLabel)
    }

    // -------------------------------------------------------------------------
    // Scenario: success snackbar displays "OK" as the action label
    // -------------------------------------------------------------------------
    // Given: a SnackbarVisualsWithError where isError = false
    // When:  actionLabel is read
    // Then:  it returns "OK"
    @Test
    fun `given isError is false, actionLabel is OK`() {
        val visuals = SnackbarVisualsWithError(message = "Operation succeeded", isError = false)
        assertEquals("OK", visuals.actionLabel)
    }

    // -------------------------------------------------------------------------
    // Scenario: snackbar never shows an automatic dismiss button
    // -------------------------------------------------------------------------
    // Given: any SnackbarVisualsWithError
    // When:  withDismissAction is read
    // Then:  it is always false (the user cannot dismiss it with a dedicated button)
    @Test
    fun `given any SnackbarVisualsWithError, withDismissAction is always false`() {
        val visuals = SnackbarVisualsWithError(message = "Test", isError = false)
        assertFalse(visuals.withDismissAction)
    }

    // -------------------------------------------------------------------------
    // Scenario: snackbar stays visible until explicitly dismissed
    // -------------------------------------------------------------------------
    // Given: any SnackbarVisualsWithError
    // When:  duration is read
    // Then:  it is SnackbarDuration.Indefinite — it won't auto-hide
    @Test
    fun `given any SnackbarVisualsWithError, duration is Indefinite`() {
        val visuals = SnackbarVisualsWithError(message = "Test", isError = true)
        assertEquals(SnackbarDuration.Indefinite, visuals.duration)
    }

    // -------------------------------------------------------------------------
    // Scenario: Mockito mock vs real implementation — educational comparison
    // -------------------------------------------------------------------------
    // Given:  a Mockito mock of SnackbarVisuals configured to return "Custom"
    //         AND a real SnackbarVisualsWithError(isError = true)
    // When:   actionLabel is read from both
    // Then:   the mock returns whatever we configured ("Custom")
    //         the real class enforces its own invariant ("Error")
    //
    // KEY LESSON: mocks let tests control collaborator behavior in isolation.
    // They are NOT meant to replace testing the real class itself.
    @Test
    fun `given mock and real visuals, they return different actionLabels`() {
        // Mockito creates an object that implements SnackbarVisuals without any real logic.
        // We then teach it what to return using whenever().thenReturn().
        val mockVisuals = mock<SnackbarVisuals>()
        whenever(mockVisuals.actionLabel).thenReturn("Custom")

        val realVisuals = SnackbarVisualsWithError(message = "Real error", isError = true)

        assertEquals("Custom", mockVisuals.actionLabel) // returns what we told it to
        assertEquals("Error", realVisuals.actionLabel) // returns what the class dictates
        assertNotEquals(mockVisuals.actionLabel, realVisuals.actionLabel)
    }
}
