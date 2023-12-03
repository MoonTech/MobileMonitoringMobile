package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.RoomCamera

@Composable
fun TransmittingInfoPanel(
    modifier: Modifier = Modifier,
    isStreaming: Boolean,
    roomCamera: RoomCamera?
) {
    Row(modifier = modifier) {
        if (roomCamera != null) {
            Text(
                text = "Room: ${roomCamera.code}",
                modifier = Modifier.padding(end = 10.dp),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Camera: ${roomCamera.token}",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
//        Text(text = "Transmission ${if (isStreaming) "on" else "off"}")
    }
}