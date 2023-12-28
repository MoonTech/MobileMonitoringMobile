package com.example.moontech.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.RoomCamera

@Composable
fun TransmittingInfoPanel(
    modifier: Modifier = Modifier,
    isStreaming: Boolean,
    roomCamera: RoomCamera?,
    onSwitchRoom: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        if (roomCamera != null) {
            RoomCameraHeader(
                roomCode = roomCamera.code,
                cameraName = roomCamera.name,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .fillMaxWidth(0.75f)
            )
        } else {
            Text(
                text = "Select a room",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        MenuDrawer(items = listOf("Select room"), header = { /*TODO*/ }) { closeMenu, _ ->
            Text(text = if (roomCamera == null) "Select camera" else "Switch camera",
                Modifier
                    .clickable(enabled = !isStreaming) {
                        closeMenu()
                        onSwitchRoom()
                    }
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                    .fillMaxWidth()
            )
        }
    }
}