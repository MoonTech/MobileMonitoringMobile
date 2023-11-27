package com.example.moontech.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class ButtonsKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun primaryButton() {
        composeTestRule.setContent {
            PrimaryButton(text = "Test", onClick = {  })
        }
        composeTestRule.onNodeWithText("Test")
            .assertHasClickAction()
    }

    @Test
    fun floatingActionButtonWithIcon() {
        composeTestRule.setContent {
            FloatingActionButtonWithIcon(
                onClick = { /*TODO*/ },
                icon = Icons.Filled.LiveTv,
                contentDescription = "Test description"
            )
        }
        composeTestRule.onNodeWithContentDescription("Test description")
            .assertHasClickAction()
        composeTestRule.onRoot().printToLog("TAG")
    }
}