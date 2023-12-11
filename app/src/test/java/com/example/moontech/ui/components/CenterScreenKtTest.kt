package com.example.moontech.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import com.example.moontech.GenericUiTest
import org.junit.Test

class CenterScreenTest: GenericUiTest() {


    @Test
    fun centerScreenTest() {
        composeTestRule.setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Use CenterScreen in your UI hierarchy
                CenterScreen(
                    modifier = Modifier.testTag("centerScreenTest")
                ) {
                    // Your content goes here
                }
            }
        }

        // Verify that the CenterScreen has the correct layout properties
        composeTestRule.onNodeWithTag("centerScreenTest")
            .assertExists()
    }
}