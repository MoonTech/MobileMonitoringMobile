package com.example.moontech.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.RoomCamera

@Composable
fun BoxScope.TransmittingControls(
    roomCamera: RoomCamera?,
    isStreaming: Boolean,
    onStartStream: () -> Unit,
    onStopStream: () -> Unit,

) {
    ElevatedButton(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 4.dp),
        onClick = {
            if (isStreaming) {
                onStopStream()
            } else {
                onStartStream()
            }
        },
        enabled = roomCamera != null
    ) {
        Text(text = if (!isStreaming) "Start transmission" else "Stop transmission")
    }
}