package com.example.moontech.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import org.junit.Rule
import org.junit.Test

class DismissableRoomTileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplay() {
        val room = object: ObjectWithRoomCode {
            override val code: String = "Test code"
        }
        composeTestRule.setContent {
            DismissableRoomTile(room = room, onDismiss = {})
        }
        composeTestRule.onNodeWithText("Test code")
            .assertExists()
    }
}