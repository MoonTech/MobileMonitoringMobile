package com.example.moontech.ui.screens.watch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.base.RoomListBase
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun WatchMainScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val rooms by viewModel.myRooms.collectAsState()
    RoomListBase(rooms = rooms, onClick = { /*TODO*/ })
}

