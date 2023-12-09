package com.example.moontech.ui.screens.base

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.RoomCard

@Composable
fun <T : ObjectWithRoomCode> MainScreenBase(
    rooms: List<T>,
    addRoom: () -> Unit,
    modifier: Modifier = Modifier,
    onSettings: (room: T) -> Unit,
    onClick: (room: T) -> Unit,
) {
    ListScreenBase(
        modifier = modifier,
        addRoom = addRoom,
        rooms = rooms,
        floatingActionButtonVisible = true,
    ) { room ->
        RoomCard(
            modifier = Modifier.clickable { onClick(room) },
            room = room, rowTitleContent = {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Setting")
            }) { }
    }
}