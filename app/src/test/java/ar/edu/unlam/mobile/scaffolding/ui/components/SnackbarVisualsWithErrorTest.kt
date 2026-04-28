package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.material3.SnackbarDuration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Unit tests for [SnackbarVisualsWithError].
 *
 * [SnackbarVisualsWithError] is a pure Kotlin class with no Android-framework
 * dependencies at runtime, so all tests run on the JVM — no device or emulator needed.
 *
 * Properties under test:
 *   - [SnackbarVisualsWithError.message]         — stored as-is from constructor
 *   - [SnackbarVisualsWithError.actionLabel]      — "Error" when isError = true, "OK" otherwise
 *   - [SnackbarVisualsWithError.withDismissAction] — always false (no dismiss button)
 *   - [SnackbarVisualsWithError.duration]          — always [SnackbarDuration.Indefinite]
 *
 * Pattern used — Gherkin (BDD):
 *   Given  the initial context / precondition
 *   When   the action under test
 *   Then   the expected observable result
 */
class SnackbarVisualsWithErrorTest {
    // -------------------------------------------------------------------------
    // Scenario: message is stored correctly
    // -------------------------------------------------------------------------
    // Given: a SnackbarVisualsWithError created with a specific message
    // When:  message is read
    // Then:  it equals the value passed to the constructor
    @Test
    fun `given a message string, message property returns that string`() {
        val visuals = SnackbarVisualsWithError(message = "Something went wrong", isError = true)

        assertEquals("Something went wrong", visuals.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: error snackbar action label is "Error"
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
    // Scenario: success snackbar action label is "OK"
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
    // Then:  it is always false — no dedicated dismiss button is rendered
    @Test
    fun `given any SnackbarVisualsWithError, withDismissAction is always false`() {
        val errorVisuals = SnackbarVisualsWithError(message = "Error case", isError = true)
        val successVisuals = SnackbarVisualsWithError(message = "Success case", isError = false)

        assertFalse(errorVisuals.withDismissAction)
        assertFalse(successVisuals.withDismissAction)
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
    // Scenario: isError flag drives actionLabel — both branches covered
    // -------------------------------------------------------------------------
    // Given: two instances with different isError values
    // When:  their actionLabels are compared
    // Then:  the labels are different — confirming the branch logic is exercised
    @Test
    fun `given two instances with different isError values, actionLabels differ`() {
        val errorVisuals = SnackbarVisualsWithError(message = "msg", isError = true)
        val successVisuals = SnackbarVisualsWithError(message = "msg", isError = false)

        assertEquals("Error", errorVisuals.actionLabel)
        assertEquals("OK", successVisuals.actionLabel)
    }
}
