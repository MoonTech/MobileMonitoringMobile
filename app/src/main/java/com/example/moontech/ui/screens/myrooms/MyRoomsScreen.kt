package com.example.moontech.ui.screens.myrooms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moontech.ui.screens.base.ListScreenBase
import com.example.moontech.ui.screens.common.RoomType
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun MyRoomsScreen(addRoom: () -> Unit, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val rooms by viewModel.myRooms.collectAsState()
    LaunchedEffect(true) {
        viewModel.fetchMyRooms()
    }
    ListScreenBase(
        modifier = modifier,
        onRoomClick = { /*TODO*/ },
        addRoom = addRoom,
        rooms = mapOf(Pair(RoomType.MY_ROOM, rooms)),
        floatingActionButtonVisible = true,
    )
}