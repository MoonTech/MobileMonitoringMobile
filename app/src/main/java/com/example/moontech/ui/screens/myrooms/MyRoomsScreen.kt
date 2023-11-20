package com.example.moontech.ui.screens.myrooms

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.base.RoomListBase
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun MyRoomsScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) = CenterScreen(modifier) {
    val rooms by viewModel.myRooms.collectAsState()
    LaunchedEffect(true) {
        viewModel.fetchMyRooms()
    }
    RoomListBase(rooms = rooms, onClick = { }, modifier = Modifier.fillMaxSize())
}