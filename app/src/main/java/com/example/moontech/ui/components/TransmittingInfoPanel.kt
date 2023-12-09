package com.example.moontech.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showMenu by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        if (roomCamera != null) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    text = roomCamera.code,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
                Text(
                    text = "Camera: ${roomCamera.name}",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
        } else {
            Text(
                text = "Select a room",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { showMenu = true }
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Switch room")
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                Text(text = if (roomCamera == null) "Select room" else "Switch room",
                    Modifier
                        .clickable(enabled = !isStreaming) {
                            showMenu = false
                            onSwitchRoom()
                        }
                        .padding(start = 8.dp, end = 8.dp)
                )
            }
        }
    }
}