package com.example.moontech.ui.screens.base

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.FloatingActionButtonWithIcon
import com.example.moontech.ui.components.base.RoomListBase

@Composable
fun <T : ObjectWithRoomCode> ListScreenBase(
    modifier: Modifier = Modifier,
    addRoom: () -> Unit = {},
    rooms: List<T>,
    floatingActionButtonVisible: Boolean = false,
    topBar: @Composable () -> Unit = {},
    content: @Composable (room: T) -> Unit
) {
    Scaffold(
        topBar = { topBar() },
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButtonWithIcon(
                visible = floatingActionButtonVisible,
                onClick = addRoom,
                icon = Icons.Filled.Add,
                contentDescription = "Add room"
            )
        }
    ) {
        CenterScreen(modifier = Modifier.padding(it)) {
            RoomListBase(
                rooms = rooms,
                modifier = Modifier.fillMaxSize(),
            ) { room ->
                content(room)
            }
        }
    }
}