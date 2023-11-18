package com.example.moontech.ui.screens.watch

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.base.RoomListBase
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun WatchMainScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) = CenterScreen(modifier) {
    val rooms by viewModel.watchedRooms.collectAsState()
    RoomListBase(rooms = rooms, onClick = { }, modifier = Modifier.fillMaxSize())
}

