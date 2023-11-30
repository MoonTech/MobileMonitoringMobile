package com.example.moontech.ui.screens.myrooms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.RoomButtons
import com.example.moontech.ui.components.RoomCard
import com.example.moontech.ui.screens.base.ListScreenBase
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun MyRoomsScreen(addRoom: () -> Unit, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val rooms by viewModel.myRooms.collectAsState()
    LaunchedEffect(true) {
        viewModel.fetchMyRooms()
    }
    ListScreenBase(        modifier = modifier,
        onRoomClick = { /*TODO*/ },
        addRoom = addRoom,
        rooms = rooms,
        floatingActionButtonVisible = true,
    ) { room ->
        RoomCard(room = room, rowTitleContent = {
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Setting")
            }
        }) {
            RoomButtons(modifier = Modifier.fillMaxWidth(), onWatch = {

            }, onTransmit = {

            })
        }
    }
}