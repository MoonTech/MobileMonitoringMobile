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
    topBar: @Composable () -> Unit,
    onClick: (room: T) -> Unit,
    floatingActionButtonVisible: Boolean = true,
    rowTileContent: @Composable (room: T) -> Unit = {
        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Setting")
    }
) {
    ListScreenBase(
        modifier = modifier,
        addRoom = addRoom,
        rooms = rooms,
        floatingActionButtonVisible = floatingActionButtonVisible,
        topBar = topBar,
    ) { room ->
        RoomCard(
            modifier = Modifier.clickable { onClick(room) },
            room = room, rowTitleContent = { rowTileContent(room) }) { }
    }
}