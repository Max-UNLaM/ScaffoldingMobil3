package ar.edu.unlam.mobile.scaffolding.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for all UIState data classes and sealed interfaces in the app.
 *
 * These are pure Kotlin types with no Android-framework dependencies, so all tests
 * run on the JVM — no device or emulator needed.
 *
 * Types under test:
 *   - [HelloMessageUIState] (sealed interface with Success, Loading, Error)
 *   - [HomeUIState]         (data class wrapping HelloMessageUIState)
 *   - [TextListUIState]     (sealed interface with Success, Loading, Error)
 *   - [UserNameUIState]     (sealed interface with Success, Loading, Error)
 *   - [UserUIState]         (data class wrapping TextListUIState + UserNameUIState)
 *   - [ValidationResult]   (data class with isValid + message)
 *
 * Pattern used — Gherkin (BDD):
 *   Given  the initial context / precondition
 *   When   the action under test
 *   Then   the expected observable result
 */
class UIStateTest {

    // =========================================================================
    // HelloMessageUIState
    // =========================================================================

    // -------------------------------------------------------------------------
    // Scenario: Success variant holds and returns its message
    // -------------------------------------------------------------------------
    // Given: a HelloMessageUIState.Success with message "hello"
    // When:  the message property is accessed
    // Then:  it equals "hello"
    @Test
    fun `given HelloMessageUIState Success, message property matches constructor arg`() {
        val state = HelloMessageUIState.Success("hello")

        assertEquals("hello", state.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: Two Success instances with the same message are equal
    // -------------------------------------------------------------------------
    // Given: two HelloMessageUIState.Success objects with identical messages
    // When:  they are compared with ==
    // Then:  they are equal (data class structural equality)
    @Test
    fun `given two HelloMessageUIState Success with same message, they are equal`() {
        val a = HelloMessageUIState.Success("2b")
        val b = HelloMessageUIState.Success("2b")

        assertEquals(a, b)
    }

    // -------------------------------------------------------------------------
    // Scenario: Success instances with different messages are not equal
    // -------------------------------------------------------------------------
    // Given: two HelloMessageUIState.Success objects with different messages
    // When:  they are compared with ==
    // Then:  they are not equal
    @Test
    fun `given two HelloMessageUIState Success with different messages, they are not equal`() {
        val a = HelloMessageUIState.Success("2b")
        val b = HelloMessageUIState.Success("9s")

        assertNotEquals(a, b)
    }

    // -------------------------------------------------------------------------
    // Scenario: Loading is a singleton data object
    // -------------------------------------------------------------------------
    // Given: two references to HelloMessageUIState.Loading
    // When:  they are compared with ===
    // Then:  they are the same instance (data object identity)
    @Test
    fun `given HelloMessageUIState Loading, it is always the same singleton instance`() {
        val a: HelloMessageUIState = HelloMessageUIState.Loading
        val b: HelloMessageUIState = HelloMessageUIState.Loading

        assertTrue(a === b)
    }

    // -------------------------------------------------------------------------
    // Scenario: Error variant holds its message
    // -------------------------------------------------------------------------
    // Given: a HelloMessageUIState.Error with a specific message
    // When:  the message property is accessed
    // Then:  it returns the expected string
    @Test
    fun `given HelloMessageUIState Error, message property matches constructor arg`() {
        val state = HelloMessageUIState.Error("network failure")

        assertEquals("network failure", state.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: distinct variants are not equal to each other
    // -------------------------------------------------------------------------
    // Given: a Success, Loading, and Error variant for HelloMessageUIState
    // When:  they are compared pairwise
    // Then:  none of them are equal to the others
    @Test
    fun `given different HelloMessageUIState variants, none are equal`() {
        val success: HelloMessageUIState = HelloMessageUIState.Success("msg")
        val loading: HelloMessageUIState = HelloMessageUIState.Loading
        val error: HelloMessageUIState = HelloMessageUIState.Error("msg")

        assertNotEquals(success, loading)
        assertNotEquals(success, error)
        assertNotEquals(loading, error)
    }

    // =========================================================================
    // HomeUIState
    // =========================================================================

    // -------------------------------------------------------------------------
    // Scenario: HomeUIState stores its wrapped state
    // -------------------------------------------------------------------------
    // Given: a HomeUIState wrapping a HelloMessageUIState.Success
    // When:  helloMessageState is accessed
    // Then:  it equals the value passed to the constructor
    @Test
    fun `given HomeUIState with Success, helloMessageState returns the wrapped state`() {
        val inner = HelloMessageUIState.Success("2b")
        val homeState = HomeUIState(helloMessageState = inner)

        assertEquals(inner, homeState.helloMessageState)
    }

    // -------------------------------------------------------------------------
    // Scenario: HomeUIState copy produces an updated value
    // -------------------------------------------------------------------------
    // Given: a HomeUIState wrapping Loading
    // When:  copy() is called to change to Success
    // Then:  the copy holds the new state and the original is unchanged
    @Test
    fun `given HomeUIState Loading, copy to Success yields a new state`() {
        val original = HomeUIState(helloMessageState = HelloMessageUIState.Loading)
        val updated = original.copy(helloMessageState = HelloMessageUIState.Success("2b"))

        assertEquals(HelloMessageUIState.Loading, original.helloMessageState)
        assertEquals(HelloMessageUIState.Success("2b"), updated.helloMessageState)
    }

    // =========================================================================
    // TextListUIState
    // =========================================================================

    // -------------------------------------------------------------------------
    // Scenario: Success variant holds the list
    // -------------------------------------------------------------------------
    // Given: a TextListUIState.Success with a list of two items
    // When:  the list property is accessed
    // Then:  it contains exactly those two items
    @Test
    fun `given TextListUIState Success, list property matches the provided list`() {
        val list = mutableListOf("Alpha", "Beta")
        val state = TextListUIState.Success(list)

        assertEquals(list, state.list)
    }

    // -------------------------------------------------------------------------
    // Scenario: Loading is a singleton data object
    // -------------------------------------------------------------------------
    // Given: two references to TextListUIState.Loading
    // When:  they are compared with ===
    // Then:  they are the same instance
    @Test
    fun `given TextListUIState Loading, it is always the same singleton instance`() {
        val a: TextListUIState = TextListUIState.Loading
        val b: TextListUIState = TextListUIState.Loading

        assertTrue(a === b)
    }

    // -------------------------------------------------------------------------
    // Scenario: Error variant stores its message
    // -------------------------------------------------------------------------
    // Given: a TextListUIState.Error with message "se rompio todo"
    // When:  the message property is accessed
    // Then:  it returns "se rompio todo"
    @Test
    fun `given TextListUIState Error, message property matches constructor arg`() {
        val state = TextListUIState.Error("se rompio todo")

        assertEquals("se rompio todo", state.message)
    }

    // =========================================================================
    // UserNameUIState
    // =========================================================================

    // -------------------------------------------------------------------------
    // Scenario: Success variant holds the user name
    // -------------------------------------------------------------------------
    // Given: a UserNameUIState.Success with name "2b"
    // When:  the name property is accessed
    // Then:  it returns "2b"
    @Test
    fun `given UserNameUIState Success, name property matches constructor arg`() {
        val state = UserNameUIState.Success("2b")

        assertEquals("2b", state.name)
    }

    // -------------------------------------------------------------------------
    // Scenario: Loading is a singleton data object
    // -------------------------------------------------------------------------
    // Given: two references to UserNameUIState.Loading
    // When:  they are compared with ===
    // Then:  they are the same instance
    @Test
    fun `given UserNameUIState Loading, it is always the same singleton instance`() {
        val a: UserNameUIState = UserNameUIState.Loading
        val b: UserNameUIState = UserNameUIState.Loading

        assertTrue(a === b)
    }

    // -------------------------------------------------------------------------
    // Scenario: Error variant stores its message
    // -------------------------------------------------------------------------
    // Given: a UserNameUIState.Error with a specific message
    // When:  the message property is accessed
    // Then:  it returns the expected string
    @Test
    fun `given UserNameUIState Error, message property matches constructor arg`() {
        val state = UserNameUIState.Error("not found")

        assertEquals("not found", state.message)
    }

    // =========================================================================
    // UserUIState
    // =========================================================================

    // -------------------------------------------------------------------------
    // Scenario: UserUIState stores both wrapped states
    // -------------------------------------------------------------------------
    // Given: a UserUIState with Loading for both fields
    // When:  each property is accessed
    // Then:  both equal the values passed to the constructor
    @Test
    fun `given UserUIState with both Loading, both properties reflect Loading`() {
        val state = UserUIState(
            textListState = TextListUIState.Loading,
            userNameState = UserNameUIState.Loading,
        )

        assertEquals(TextListUIState.Loading, state.textListState)
        assertEquals(UserNameUIState.Loading, state.userNameState)
    }

    // -------------------------------------------------------------------------
    // Scenario: UserUIState copy changes only the specified field
    // -------------------------------------------------------------------------
    // Given: a UserUIState with both fields Loading
    // When:  copy() is called to update only userNameState to Success
    // Then:  userNameState is Success and textListState is still Loading
    @Test
    fun `given UserUIState Loading, copy to update userNameState leaves textListState unchanged`() {
        val original = UserUIState(
            textListState = TextListUIState.Loading,
            userNameState = UserNameUIState.Loading,
        )

        val updated = original.copy(userNameState = UserNameUIState.Success("2b"))

        assertEquals(TextListUIState.Loading, updated.textListState)
        assertEquals(UserNameUIState.Success("2b"), updated.userNameState)
    }

    // -------------------------------------------------------------------------
    // Scenario: two UserUIState instances with the same fields are equal
    // -------------------------------------------------------------------------
    // Given: two UserUIState objects with identical field values
    // When:  they are compared with ==
    // Then:  they are equal (data class structural equality)
    @Test
    fun `given two UserUIState with same fields, they are equal`() {
        val a = UserUIState(
            textListState = TextListUIState.Loading,
            userNameState = UserNameUIState.Loading,
        )
        val b = UserUIState(
            textListState = TextListUIState.Loading,
            userNameState = UserNameUIState.Loading,
        )

        assertEquals(a, b)
    }

    // =========================================================================
    // ValidationResult
    // =========================================================================

    // -------------------------------------------------------------------------
    // Scenario: ValidationResult stores isValid and message
    // -------------------------------------------------------------------------
    // Given: a ValidationResult with isValid = true and a message
    // When:  both properties are accessed
    // Then:  they equal the constructor arguments
    @Test
    fun `given ValidationResult valid, properties reflect the constructor arguments`() {
        val result = ValidationResult(isValid = true, message = "All good")

        assertTrue(result.isValid)
        assertEquals("All good", result.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: invalid ValidationResult stores isValid = false
    // -------------------------------------------------------------------------
    // Given: a ValidationResult with isValid = false
    // When:  isValid is accessed
    // Then:  it is false
    @Test
    fun `given ValidationResult invalid, isValid is false`() {
        val result = ValidationResult(isValid = false, message = "Something failed")

        assertFalse(result.isValid)
    }

    // -------------------------------------------------------------------------
    // Scenario: two ValidationResult instances with the same fields are equal
    // -------------------------------------------------------------------------
    // Given: two ValidationResult objects with identical field values
    // When:  they are compared with ==
    // Then:  they are equal (data class structural equality)
    @Test
    fun `given two ValidationResult with same fields, they are equal`() {
        val a = ValidationResult(isValid = true, message = "OK")
        val b = ValidationResult(isValid = true, message = "OK")

        assertEquals(a, b)
    }

    // -------------------------------------------------------------------------
    // Scenario: ValidationResult copy changes only the specified field
    // -------------------------------------------------------------------------
    // Given: a valid ValidationResult
    // When:  copy() is called to change isValid to false
    // Then:  isValid is false and message is unchanged
    @Test
    fun `given ValidationResult, copy to change isValid leaves message unchanged`() {
        val original = ValidationResult(isValid = true, message = "Formulario válido 😎")
        val updated = original.copy(isValid = false)

        assertFalse(updated.isValid)
        assertEquals("Formulario válido 😎", updated.message)
    }
}
