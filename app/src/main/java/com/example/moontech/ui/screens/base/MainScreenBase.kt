package com.example.moontech.ui.screens.base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.RoomCard

@Composable
fun <T: ObjectWithRoomCode> MainScreenBase(
    rooms: List<T>,
    addRoom: () -> Unit,
    modifier: Modifier = Modifier,
    onTransmit: (room: T) -> Unit,
    onWatch: (room: T) -> Unit,
    onSettings: (room: T) -> Unit,
) {
    ListScreenBase(
        modifier = modifier,
        onRoomClick = { /*TODO*/ },
        addRoom = addRoom,
        rooms = rooms,
        floatingActionButtonVisible = true,
    ) { room ->
        RoomCard(room = room, rowTitleContent = {
            IconButton(onClick = {
                onSettings(room)
            }) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Setting")
            }
        }) { }
    }
}