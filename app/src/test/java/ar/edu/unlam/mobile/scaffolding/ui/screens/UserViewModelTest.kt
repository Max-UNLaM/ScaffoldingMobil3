package ar.edu.unlam.mobile.scaffolding.ui.screens

import ar.edu.unlam.mobile.scaffolding.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [UserViewModel].
 *
 * The ViewModel performs two sequential delayed state transitions inside [viewModelScope]:
 *
 *   t = 0 ms   → userNameState = Loading,  textListState = Loading
 *   t = 2000ms → userNameState = Success("2b"), textListState still Loading
 *   t = 4000ms → userNameState = Success("2b"), textListState = Error("se rompio todo")
 *
 * Because we pass [mainDispatcherRule.testDispatcher] to [runTest], both the test scope
 * and [kotlinx.coroutines.Dispatchers.Main] share the same virtual clock.
 * [advanceTimeBy] skips virtual time — no real waiting occurs.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // -------------------------------------------------------------------------
    // Scenario: Both UI sections start in Loading state
    // -------------------------------------------------------------------------
    // Given: a newly created UserViewModel (t = 0)
    // When:  uiState is read before any delay elapses
    // Then:  userNameState is Loading
    @Test
    fun `given a new UserViewModel, initial userNameState is Loading`() {
        val viewModel = UserViewModel()
        assert(viewModel.uiState.value.userNameState is UserNameUIState.Loading)
    }

    // Given: a newly created UserViewModel (t = 0)
    // When:  uiState is read before any delay elapses
    // Then:  textListState is Loading
    @Test
    fun `given a new UserViewModel, initial textListState is Loading`() {
        val viewModel = UserViewModel()
        assert(viewModel.uiState.value.textListState is TextListUIState.Loading)
    }

    // -------------------------------------------------------------------------
    // Scenario: After 2 000 ms the user name resolves
    // -------------------------------------------------------------------------
    // Given: a UserViewModel and 2 001 ms of virtual time
    // When:  the first delay(2000) completes
    // Then:  userNameState transitions to Success("2b")
    @Test
    fun `given 2000ms pass, userNameState becomes Success with 2b`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = UserViewModel()

            // advanceTimeBy moves the virtual clock forward without real-time waiting
            advanceTimeBy(2_001)

            assertEquals(
                UserNameUIState.Success("2b"),
                viewModel.uiState.value.userNameState,
            )
        }

    // -------------------------------------------------------------------------
    // Scenario: textList is still Loading while only the first delay has elapsed
    // -------------------------------------------------------------------------
    // Given: 2 001 ms of virtual time (first delay done, second not started)
    // When:  userNameState is already Success
    // Then:  textListState remains Loading (the second delay hasn't expired)
    @Test
    fun `given only 2000ms pass, textListState remains Loading`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = UserViewModel()

            advanceTimeBy(2_001)

            // The second delay(2000) has not elapsed yet — still Loading
            assertFalse(viewModel.uiState.value.textListState is TextListUIState.Error)
            assert(viewModel.uiState.value.textListState is TextListUIState.Loading)
        }

    // -------------------------------------------------------------------------
    // Scenario: After 4 000 ms the text list transitions to Error
    // -------------------------------------------------------------------------
    // Given: 4 001 ms of virtual time (both delays done)
    // When:  the second delay(2000) completes
    // Then:  textListState becomes Error with the message "se rompio todo"
    @Test
    fun `given 4000ms pass, textListState becomes Error with message`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = UserViewModel()

            advanceTimeBy(4_001)

            val textListState = viewModel.uiState.value.textListState
            assert(textListState is TextListUIState.Error)
            assertEquals(
                "se rompio todo",
                (textListState as TextListUIState.Error).message,
            )
        }

    // -------------------------------------------------------------------------
    // Scenario: userNameState is unchanged after the second delay
    // -------------------------------------------------------------------------
    // Given: 4 001 ms of virtual time
    // When:  both delays have elapsed
    // Then:  userNameState is still Success("2b") (second coroutine did not overwrite it)
    @Test
    fun `given 4000ms pass, userNameState is still Success`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = UserViewModel()

            advanceTimeBy(4_001)

            assertEquals(
                UserNameUIState.Success("2b"),
                viewModel.uiState.value.userNameState,
            )
        }
}
