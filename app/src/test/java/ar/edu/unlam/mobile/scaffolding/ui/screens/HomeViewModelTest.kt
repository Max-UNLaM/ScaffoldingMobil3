package ar.edu.unlam.mobile.scaffolding.ui.screens

import ar.edu.unlam.mobile.scaffolding.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [HomeViewModel].
 *
 * These tests run on the JVM with no Android context. The [MainDispatcherRule] replaces
 * [kotlinx.coroutines.Dispatchers.Main] so that [androidx.lifecycle.viewModelScope]
 * coroutines execute in a controlled environment.
 *
 * Pattern used — Gherkin (BDD):
 *   Given  the initial context / precondition
 *   When   the action under test
 *   Then   the expected observable result
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // -------------------------------------------------------------------------
    // Scenario: ViewModel publishes a successful greeting on creation
    // -------------------------------------------------------------------------
    // Given: a freshly created HomeViewModel
    // When:  uiState is observed immediately after construction
    // Then:  helloMessageState is Success and carries the message "2b"
    @Test
    fun `given a new HomeViewModel, initial state is Success with message 2b`() {
        val viewModel = HomeViewModel()

        val state = viewModel.uiState.value

        assertEquals(HelloMessageUIState.Success("2b"), state.helloMessageState)
    }

    // -------------------------------------------------------------------------
    // Scenario: ViewModel does NOT stay in Loading after init
    // -------------------------------------------------------------------------
    // Given: a HomeViewModel whose init block runs synchronously
    // When:  the init block completes
    // Then:  the state must not be Loading (Loading is only the intermediate value)
    @Test
    fun `given a new HomeViewModel, state is not Loading after init`() {
        val viewModel = HomeViewModel()
        assertFalse(viewModel.uiState.value.helloMessageState is HelloMessageUIState.Loading)
    }

    // -------------------------------------------------------------------------
    // Scenario: ViewModel does not produce an error in the happy path
    // -------------------------------------------------------------------------
    // Given: a HomeViewModel with no failing dependencies
    // When:  uiState is read after init
    // Then:  state is not Error
    @Test
    fun `given a new HomeViewModel, state is not Error after init`() {
        val viewModel = HomeViewModel()
        assertFalse(viewModel.uiState.value.helloMessageState is HelloMessageUIState.Error)
    }

    // -------------------------------------------------------------------------
    // Scenario: The exposed StateFlow is read-only (structural check)
    // -------------------------------------------------------------------------
    // Given: the uiState property
    // When:  its type is inspected
    // Then:  it exposes a StateFlow, not a MutableStateFlow
    //        (enforced by asStateFlow() — outside callers cannot emit new values)
    @Test
    fun `given HomeViewModel, uiState is a read-only StateFlow`() {
        val viewModel = HomeViewModel()
        // If uiState were a MutableStateFlow it would be castable; it must not be.
        assertFalse(viewModel.uiState is kotlinx.coroutines.flow.MutableStateFlow)
    }
}
