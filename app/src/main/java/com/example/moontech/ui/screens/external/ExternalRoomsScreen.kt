package com.example.moontech.ui.screens.external

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.RoomButtons
import com.example.moontech.ui.components.RoomCard
import com.example.moontech.ui.screens.base.ListScreenBase
import com.example.moontech.ui.viewmodel.AppViewModel

private const val TAG = "ExternalRoomsScreen"

@Composable
fun ExternalRoomsScreen(
    addRoom: () -> Unit,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    onTransmit: (room: ObjectWithRoomCode) -> Unit,
    onWatch: (room: ObjectWithRoomCode) -> Unit,
    onSettings: (room: ObjectWithRoomCode) -> Unit,
) {
    val rooms by viewModel.externalRooms.collectAsState()
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
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Setting")
            }
        }) {
            RoomButtons(modifier = Modifier.fillMaxWidth(), onWatch = {
                onWatch(room)
            }, onTransmit = {
                onTransmit(room)
            })
        }
    }
}