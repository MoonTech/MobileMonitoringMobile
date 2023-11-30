package com.example.moontech.ui.screens.transmit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.screens.common.RoomType
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun TransmitMainScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    addRoom: () -> Unit,
    onClick: (room: ObjectWithRoomCode) -> Unit
) {
    val externalCameras by viewModel.externalRoomCameras.collectAsState()
    val myCameras by viewModel.myRoomCameras.collectAsState()
    val myRooms by viewModel.myRooms.collectAsState()
    val rooms = mapOf(
        Pair(RoomType.EXTERNAL, externalCameras),
        Pair(RoomType.MY_ROOMS, myRooms)
    )
    var selectedTab by rememberSaveable {
        mutableStateOf(RoomType.MY_ROOMS)
    }

}