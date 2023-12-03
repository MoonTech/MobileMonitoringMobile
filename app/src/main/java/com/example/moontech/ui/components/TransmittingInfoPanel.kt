package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.RoomCamera

@Composable
fun TransmittingInfoPanel(
    modifier: Modifier = Modifier,
    isStreaming: Boolean,
    roomCamera: RoomCamera?
) {
Column(modifier = modifier) {
    if (roomCamera != null) {
        Text(text = "Room: ${roomCamera.code}")
        Text(text = "Camera name: ${roomCamera.token}")
    }
    Text(text = "Transmission ${if (isStreaming) "on" else "off"}")
}
}