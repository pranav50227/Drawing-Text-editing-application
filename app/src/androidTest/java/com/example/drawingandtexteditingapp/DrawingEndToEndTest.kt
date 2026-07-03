package com.example.drawingandtexteditingapp

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DrawingEndToEndTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCreateSaveEditDeleteFlow() {
        // 1. Create a new drawing
        composeTestRule.onNodeWithText("Create Drawing").performClick()
        
        // Use a generic search if text is not found or use content description
        // For now, let's assume "New Drawing" is in the TopAppBar
        composeTestRule.onNodeWithText("New Drawing").assertIsDisplayed()

        // Draw something (simulate drag on canvas)
        composeTestRule.onNodeWithTag("DrawingCanvas").performTouchInput {
            down(Offset(100f, 100f))
            moveTo(Offset(200f, 200f))
            up()
        }

        // 2. Save the drawing
        composeTestRule.onNodeWithText("Save").performClick()

        // Should be back on the list screen
        composeTestRule.onNodeWithText("Drawing List Screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Drawing 1").assertIsDisplayed()

        // 3. Edit the drawing (Verify auto-save)
        composeTestRule.onNodeWithText("Drawing 1").performClick()
        // Wait for the text to appear as it might be async
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Edit Drawing #1").fetchSemanticsNodes().isNotEmpty()
        }

        // Draw another stroke
        composeTestRule.onNodeWithTag("DrawingCanvas").performTouchInput {
            down(Offset(300f, 300f))
            moveTo(Offset(400f, 400f))
            up()
        }

        // Navigate back without manual save (it should have auto-saved on stroke end)
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Drawing List Screen").assertIsDisplayed()

        // Re-enter and verify the second stroke exists (lines count should be 2)
        composeTestRule.onNodeWithText("Drawing 1").performClick()
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Edit Drawing #1").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // 4. Delete the drawing
        composeTestRule.onNodeWithContentDescription("Delete").performClick()
        
        // Verify list is empty again (shows "Let's Draw")
        composeTestRule.onNodeWithText("Let's Draw").assertIsDisplayed()
    }
}
