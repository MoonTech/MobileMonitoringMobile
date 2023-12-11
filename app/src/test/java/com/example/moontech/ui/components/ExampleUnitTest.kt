package com.example.moontech.ui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class ExampleUnitTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun basicTestCase() {
        rule.setContent { Text("Hello, World!") }
        rule
            .onNodeWithText("Hello, World!")
            .assertExists()
    }
}