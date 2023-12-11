package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.moontech.GenericUiTest
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class RoomCardKtTest : GenericUiTest() {

    @Test
    fun shouldDisplayRoomCode() {
        composeTestRule.setContent {
            val room = mockk<ObjectWithRoomCode>()
            every { room.code } returns "Test code"
            RoomCard(room = room)
        }

        composeTestRule.onNodeWithText("Test code")
            .assertExists()
    }

    @Test
    fun shouldDisplayAdditionalContent() {
        composeTestRule.setContent {
            val room = mockk<ObjectWithRoomCode>()
            every { room.code } returns "Test code"
            RoomCard(
                room = room,
                rowTitleContent = { Box(Modifier.testTag("Content 1")) },
                content = { Box(Modifier.testTag("Content 2")) }
            )
        }
        composeTestRule.onNodeWithTag("Content 1")
            .assertExists()
        composeTestRule.onNodeWithTag("Content 2")
            .assertExists()
    }
}