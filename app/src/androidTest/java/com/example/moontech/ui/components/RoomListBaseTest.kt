package com.example.moontech.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import org.junit.Rule

class RoomListBaseTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val rooms = listOf(
        object: ObjectWithRoomCode {
            override val code: String = "Code 1"
        },
        object: ObjectWithRoomCode {
            override val code: String = "Code 2"
        }
    )

//    @Test
//    fun shouldDisplay() {
//        composeTestRule.setContent {
//            RoomListBase(rooms = rooms, onClick = {  })
//        }
//        composeTestRule.onNodeWithText("Code 1").assertExists()
//        composeTestRule.onNodeWithText("Code 2").assertExists()
//    }
}