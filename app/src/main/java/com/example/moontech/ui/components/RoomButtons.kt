package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoomButtons(modifier: Modifier = Modifier, onWatch: () -> Unit, onTransmit: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        OutlinedButton(
            onClick = onWatch,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(text = "Watch")
        }
        Button(onClick = onTransmit) {
            Text(text = "Transmit")
        }
    }
}