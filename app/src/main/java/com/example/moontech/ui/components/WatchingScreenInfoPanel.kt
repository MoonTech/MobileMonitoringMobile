package com.example.moontech.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.WatchedRoom
import com.example.moontech.data.dataclasses.WatchedRoomCamera

@Composable
fun WatchingScreenInfoPanel(
    modifier: Modifier = Modifier,
    watchedRoom: WatchedRoom,
    selectedCameraName: String,
    onCameraClicked: (camera: WatchedRoomCamera) -> Unit,
    onRecordingsClicked: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RoomCameraHeader(
            roomCode = watchedRoom.code,
            cameraName = selectedCameraName,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onRecordingsClicked, modifier = Modifier.padding(end = 8.dp)) {
            Icon(imageVector = Icons.Default.Image, contentDescription = "recordings")
        }
        MenuDrawer(items = watchedRoom.connectedCameras, header = { closeMenu ->
            Text(
                text = "Select camera",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
            )
            Divider()
        }, itemContent = { closeMenu, room ->
            Text(text = room.cameraName,
                modifier = Modifier
                    .clickable {
                        onCameraClicked(room)
                        closeMenu()
                    }
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                    .fillMaxWidth())
        })
    }
}