package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose UI tests for the [TextList] component.
 *
 * [TextList] renders a [androidx.compose.foundation.lazy.LazyColumn] where each item is
 * wrapped in a [androidx.compose.material3.Card]. These tests verify that the correct
 * text nodes appear in the rendered tree.
 */
@RunWith(AndroidJUnit4::class)
class TextListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // -------------------------------------------------------------------------
    // Scenario: all items in the list are visible
    // -------------------------------------------------------------------------
    // Given: a list with three string items
    // When:  TextList is rendered
    // Then:  each item text is visible on screen
    @Test
    fun `given a list of three items, all items are displayed`() {
        val items = listOf("Hola", "Mundo", "Compose")

        // Given
        composeTestRule.setContent {
            TextList(items = items)
        }

        // Then — verify each card renders its text
        composeTestRule.onNodeWithText("Hola").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mundo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Compose").assertIsDisplayed()
    }

    // -------------------------------------------------------------------------
    // Scenario: empty list produces no visible text nodes
    // -------------------------------------------------------------------------
    // Given: an empty list
    // When:  TextList is rendered
    // Then:  no item text nodes exist (the LazyColumn is effectively empty)
    @Test
    fun `given an empty list, no item text nodes are present`() {
        composeTestRule.setContent {
            TextList(items = emptyList())
        }

        // The LazyColumn renders but contains no children
        composeTestRule
            .onNodeWithText("Hola", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    // -------------------------------------------------------------------------
    // Scenario: single-item list renders just that item
    // -------------------------------------------------------------------------
    // Given: a list with a single entry "Único"
    // When:  TextList is rendered
    // Then:  "Único" is displayed and no other unexpected text nodes appear
    @Test
    fun `given a single-item list, that item is displayed`() {
        composeTestRule.setContent {
            TextList(items = listOf("Único"))
        }

        composeTestRule.onNodeWithText("Único").assertIsDisplayed()
    }
}
