package com.example.moontech.ui.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.RoomCamera

@Composable
fun TransmittingControls(
    roomCamera: RoomCamera?,
    modifier: Modifier = Modifier,
    isStreaming: Boolean,
    onStartStream: () -> Unit,
    onStopStream: () -> Unit,
    onSwitchRoom: () -> Unit
) = CenterColumn(modifier.width(IntrinsicSize.Max)) {
    OutlinedButton(onClick = { onSwitchRoom() }, enabled = !isStreaming, modifier = Modifier.fillMaxWidth()) {
        Text(text = if (roomCamera == null) "Select room" else "Switch room")
    }
    Button(onClick = {
        if (isStreaming) {
            onStopStream()
        } else {
            onStartStream()
        }
    }, enabled = roomCamera != null) {
        Text(text = if (roomCamera == null) "Start transmission" else "Stop transmission")
    }
}