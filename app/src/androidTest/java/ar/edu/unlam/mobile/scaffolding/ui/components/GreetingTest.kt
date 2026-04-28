package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose UI tests for the [Greeting] component.
 *
 * [createComposeRule] renders the composable inside an isolated host — no Activity,
 * no navigation, no ViewModel required. This makes these tests extremely fast and
 * focused on a single unit of UI.
 *
 * Gherkin structure is expressed as inline comments inside each test.
 */
@RunWith(AndroidJUnit4::class)
class GreetingTest {

    // createComposeRule() sets up a minimal Compose host for the test.
    @get:Rule
    val composeTestRule = createComposeRule()

    // -------------------------------------------------------------------------
    // Scenario: Greeting displays the correct name
    // -------------------------------------------------------------------------
    // Given: the Greeting composable is rendered with name = "2B"
    // When:  the UI settles
    // Then:  the text "Hello 2B!" is visible on screen
    @Test
    fun `given name 2B, Greeting displays Hello 2B`() {
        // Given
        composeTestRule.setContent {
            Greeting(name = "2B")
        }

        // Then
        composeTestRule
            .onNodeWithText("Hello 2B!")
            .assertIsDisplayed()
    }

    // -------------------------------------------------------------------------
    // Scenario: Greeting handles an empty name without crashing
    // -------------------------------------------------------------------------
    // Given: the Greeting composable is rendered with an empty string as name
    // When:  the UI settles
    // Then:  "Hello !" is visible — no crash, no missing node
    @Test
    fun `given empty name, Greeting displays Hello with exclamation only`() {
        composeTestRule.setContent {
            Greeting(name = "")
        }

        composeTestRule
            .onNodeWithText("Hello !")
            .assertIsDisplayed()
    }

    // -------------------------------------------------------------------------
    // Scenario: Greeting handles names with special characters
    // -------------------------------------------------------------------------
    // Given: a name that contains spaces and accented characters
    // When:  the UI settles
    // Then:  the full string including those characters is displayed
    @Test
    fun `given name with spaces and accents, Greeting displays it correctly`() {
        composeTestRule.setContent {
            Greeting(name = "José María")
        }

        composeTestRule
            .onNodeWithText("Hello José María!")
            .assertIsDisplayed()
    }
}
