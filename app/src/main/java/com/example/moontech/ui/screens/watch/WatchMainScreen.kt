package com.example.moontech.ui.screens.watch

import android.util.Log
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

private const val TAG = "WatchMainScreen"

@Composable
fun WatchMainScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    addRoom: () -> Unit,
    onClick: (room: ObjectWithRoomCode) -> Unit
) {
    val externalRooms by viewModel.externalRooms.collectAsState()
    val myRooms by viewModel.myRooms.collectAsState()
    val rooms = mapOf(
        Pair(RoomType.EXTERNAL, externalRooms),
        Pair(RoomType.MY_ROOMS, myRooms)
    )
    var selectedTab by rememberSaveable {
        mutableStateOf(RoomType.MY_ROOMS)
    }
    Log.i(TAG, "WatchMainScreen: ${rooms.size}")

}

