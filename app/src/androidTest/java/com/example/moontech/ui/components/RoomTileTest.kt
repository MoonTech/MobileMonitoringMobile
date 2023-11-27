package com.example.moontech.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import org.junit.Rule
import org.junit.Test

class RoomTileTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplay() {
        val room = object: ObjectWithRoomCode {
            override val code: String = "Test code"
        }
        composeTestRule.setContent {
            RoomTile(room = room)
        }
        composeTestRule.onNodeWithText("Test code")
            .assertExists()
    }
}