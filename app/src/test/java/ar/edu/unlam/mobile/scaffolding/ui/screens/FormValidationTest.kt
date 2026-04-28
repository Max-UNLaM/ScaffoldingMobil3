package ar.edu.unlam.mobile.scaffolding.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the [validateForm] top-level function.
 *
 * [validateForm] is a pure function: given the same inputs it always returns the same
 * output and has no side-effects. This makes it ideal for fast JVM unit tests —
 * no Android context, no Compose, no coroutines required.
 *
 * Validation rules:
 *   1. Name must not be empty  →  returns isValid = false
 *   2. Email must contain "@" →  returns isValid = false
 *   Both valid               →  returns isValid = true
 *
 * Edge-cases are documented inline to help students understand how the function
 * handles boundary inputs that are easy to miss.
 */
class FormValidationTest {
    // -------------------------------------------------------------------------
    // Scenario: empty name is rejected
    // -------------------------------------------------------------------------
    // Given: name is an empty string, email is a valid address
    // When:  validateForm is called
    // Then:  result is invalid with the expected Spanish message
    @Test
    fun `given empty name, when validated, then result is invalid`() {
        val result = validateForm(name = "", email = "test@example.com")

        assertFalse(result.isValid)
        assertEquals("El nombre no puede estar vacío", result.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: email without "@" is rejected
    // -------------------------------------------------------------------------
    // Given: a non-empty name but email that lacks the "@" character
    // When:  validateForm is called
    // Then:  result is invalid with the email validation message
    @Test
    fun `given email without @, when validated, then result is invalid`() {
        val result = validateForm(name = "2B", email = "invalidemail.com")

        assertFalse(result.isValid)
        assertEquals("El email debe ser válido", result.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: name validation takes priority over email validation
    // -------------------------------------------------------------------------
    // Given: both name and email are empty strings
    // When:  validateForm is called
    // Then:  the name check fires first — email is never evaluated
    //        (important: the order of guard clauses matters)
    @Test
    fun `given empty name and empty email, name error takes priority`() {
        val result = validateForm(name = "", email = "")

        assertFalse(result.isValid)
        assertEquals("El nombre no puede estar vacío", result.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: happy path — valid name and valid email
    // -------------------------------------------------------------------------
    // Given: a non-empty name and an email that contains "@"
    // When:  validateForm is called
    // Then:  result is valid with the success message
    @Test
    fun `given valid name and valid email, then result is valid`() {
        val result = validateForm(name = "2B", email = "2b@yorha.jp")

        assertTrue(result.isValid)
        assertEquals("Formulario válido 😎", result.message)
    }

    // -------------------------------------------------------------------------
    // Scenario: whitespace-only name passes isEmpty() check
    // -------------------------------------------------------------------------
    // Given: name is "   " (spaces only) and email is valid
    // When:  validateForm is called
    // Then:  result is valid — isEmpty() returns false for whitespace
    //        NOTE: this is a known limitation; a real app would use isBlank()
    @Test
    fun `given whitespace-only name, validateForm treats it as non-empty`() {
        val result = validateForm(name = "   ", email = "test@example.com")

        // The function only calls isEmpty(), not isBlank(), so "   " passes
        assertTrue(result.isValid)
    }

    // -------------------------------------------------------------------------
    // Scenario: email with multiple "@" characters still passes
    // -------------------------------------------------------------------------
    // Given: an email like "a@@b" which is technically malformed
    // When:  validateForm is called
    // Then:  result is valid because the function only checks for "@" presence,
    //        not for a fully RFC-compliant email address
    @Test
    fun `given email with multiple @ symbols, validation passes`() {
        val result = validateForm(name = "Test", email = "a@@b")

        assertTrue(result.isValid)
    }
}
