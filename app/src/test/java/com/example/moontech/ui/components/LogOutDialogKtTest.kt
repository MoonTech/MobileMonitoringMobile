package com.example.moontech.ui.components

import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.moontech.GenericUiTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LogOutDialogKtTest: GenericUiTest() {
    @Test
    fun logOutDialog_ShowDialogAndPerformActions() {
        // Mock functions
        val dismissDialogMock = mockk<() -> Unit>(relaxed = true)
        val logOutMock = mockk<() -> Unit>(relaxed = true)

        // Set up the UI with the LogOutDialog
        composeTestRule.setContent {
            LogOutDialog(dismissDialog = dismissDialogMock, logOut = logOutMock)
        }

        // Verify the appearance of the dialog
        composeTestRule
            .onNodeWithText("Are you sure that you want to log out?")
            .assertExists()
        composeTestRule.onAllNodes(isRoot()).printToLog("TAG", maxDepth = 100)

        // Perform actions
        composeTestRule
            .onNodeWithText("Log out")
            .assertExists()
            .performClick()

        composeTestRule
            .onNodeWithText("Cancel")
            .assertExists()
            .performClick()

        // Verify that the corresponding actions are called
        verify { logOutMock.invoke() }
        verify { dismissDialogMock.invoke() }
    }
}