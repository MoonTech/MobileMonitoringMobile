package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.moontech.GenericUiTest
import org.junit.Test

class MenuDrawerKtTest : GenericUiTest() {
    @Test
    fun menuDrawer_ShowMenuAndClickItems() {
        // Mock functions
        val headerMock = @Composable { closeMenu: () -> Unit -> Box(modifier = Modifier.testTag("Mocked header")) }
        val itemContentMock =
            @Composable { closeMenu: () -> Unit, item: String -> Box(modifier = Modifier.testTag(item)) }

        // Set up the UI with the MenuDrawer
        val items = listOf("Item 1", "Item 2", "Item 3")
        composeTestRule.setContent {
            MenuDrawer(
                modifier = Modifier.fillMaxSize(),
                items = items,
                header = headerMock,
                itemContent = itemContentMock
            )
        }

        // Verify the appearance of the menu icon
        composeTestRule
            .onNodeWithContentDescription("Menu")
            .performClick()

        // Click on the menu icon to open the menu
        composeTestRule
            .onNodeWithTag( "Mocked header")
            .assertExists()


        // Verify that itemContent is called for each item when clicking
        for (item in items) {
            composeTestRule
                .onNodeWithTag(item)
                .assertExists()
        }
    }
}