package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransmittingControlPanel(
    modifier: Modifier = Modifier,
    leftPanel: @Composable () -> Unit,
    rightPanel: @Composable () -> Unit
) {
    Row(modifier.padding(8.dp)) {
        leftPanel()
        Spacer(modifier = Modifier.weight(1f))
        rightPanel()
    }
}