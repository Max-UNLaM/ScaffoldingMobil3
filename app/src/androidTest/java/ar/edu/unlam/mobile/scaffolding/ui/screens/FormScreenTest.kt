package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose UI tests for [FormScreen].
 *
 * [FormScreen] is a composable with no ViewModel — it receives a [SnackbarHostState] and
 * handles validation inline. This makes it easy to test with [createComposeRule]:
 * we provide a real [SnackbarHostState] and a [Scaffold] wrapper, then assert on
 * [SnackbarHostState.currentSnackbarData] after user actions.
 *
 * Why wrap in a Scaffold?
 *   The [SnackbarHost] must be part of the composition for [SnackbarHostState.showSnackbar]
 *   to dispatch correctly. Without it the coroutine suspends but the snackbar data is never
 *   collected and currentSnackbarData remains null.
 */
@RunWith(AndroidJUnit4::class)
class FormScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper: render FormScreen inside a Scaffold that hosts the snackbar
    private fun setUpFormScreen(): SnackbarHostState {
        val snackbarHostState = SnackbarHostState()
        composeTestRule.setContent {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            ) { paddingValues ->
                FormScreen(
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
        return snackbarHostState
    }

    // -------------------------------------------------------------------------
    // Scenario: form renders all required fields and buttons
    // -------------------------------------------------------------------------
    // Given: FormScreen is displayed
    // When:  the UI settles
    // Then:  all four field labels and both buttons are visible
    @Test
    fun `given FormScreen is displayed, all fields and buttons are visible`() {
        setUpFormScreen()

        composeTestRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Repetir Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Limpiar Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enviar").assertIsDisplayed()
    }

    // -------------------------------------------------------------------------
    // Scenario: submitting without a name shows a validation error snackbar
    // -------------------------------------------------------------------------
    // Given: the name field is empty
    // When:  "Enviar" is clicked
    // Then:  a snackbar with message "El nombre no puede estar vacío" appears
    @Test
    fun `given empty name, when Enviar is clicked, then error snackbar is shown`() {
        val snackbarHostState = setUpFormScreen()

        // When
        composeTestRule.onNodeWithText("Enviar").performClick()

        // waitUntil polls until the snackbar coroutine dispatches the data
        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            snackbarHostState.currentSnackbarData != null
        }

        // Then
        assertNotNull(snackbarHostState.currentSnackbarData)
        assertEquals(
            "El nombre no puede estar vacío",
            snackbarHostState.currentSnackbarData?.visuals?.message,
        )
    }

    // -------------------------------------------------------------------------
    // Scenario: submitting with a valid name and email shows a success snackbar
    // -------------------------------------------------------------------------
    // Given: name = "2B" and email = "2b@yorha.jp" are entered
    // When:  "Enviar" is clicked
    // Then:  the success snackbar message appears
    @Test
    fun `given valid name and email, when Enviar is clicked, then success snackbar is shown`() {
        val snackbarHostState = setUpFormScreen()

        // Given — type into the fields
        composeTestRule.onNodeWithText("Ingrese su nombre completo").performTextInput("2B")
        composeTestRule.onNodeWithText("Email").performTextInput("2b@yorha.jp")

        // When
        composeTestRule.onNodeWithText("Enviar").performClick()

        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            snackbarHostState.currentSnackbarData != null
        }

        // Then
        assertEquals(
            "Formulario válido 😎",
            snackbarHostState.currentSnackbarData?.visuals?.message,
        )
    }

    // -------------------------------------------------------------------------
    // Scenario: "Limpiar Name" button clears the name field
    // -------------------------------------------------------------------------
    // Given: "2B" was typed into the name field
    // When:  "Limpiar Name" is clicked
    // Then:  the name field text is cleared (supporting text is visible again)
    @Test
    fun `given name was entered, when Limpiar Name is clicked, then field is cleared`() {
        setUpFormScreen()

        // Given
        composeTestRule.onNodeWithText("Ingrese su nombre completo").performTextInput("2B")

        // When
        composeTestRule.onNodeWithText("Limpiar Name").performClick()

        // Then — after clearing, the supporting text reappears because the field is empty
        composeTestRule.onNodeWithText("Ingrese su nombre completo").assertIsDisplayed()
    }
}
