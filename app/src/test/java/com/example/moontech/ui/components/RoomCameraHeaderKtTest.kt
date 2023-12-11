package com.example.moontech.ui.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.moontech.GenericUiTest
import org.junit.Test

class RoomCameraHeaderKtTest: GenericUiTest() {

    @Test
    fun shouldShowHeaderWithContent() {
        composeTestRule.setContent {
            RoomCameraHeader(roomCode = "Test Code", cameraName = "Test Name")
        }

        composeTestRule.onNodeWithText("Test Code")
            .assertExists()

        composeTestRule.onNodeWithText("Camera: Test Name")
            .assertExists()
    }

    @Test
    fun shouldShowEmptyHeader() {
        composeTestRule.setContent {
            RoomCameraHeader(roomCode = null, cameraName = null)
        }

        composeTestRule.onAllNodesWithText("", substring = true)
            .assertCountEquals(0)
    }
}