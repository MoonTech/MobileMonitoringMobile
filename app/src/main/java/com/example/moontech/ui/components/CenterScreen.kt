package com.example.moontech.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CenterScreen(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    CenterColumn(modifier = modifier, content = content)
}