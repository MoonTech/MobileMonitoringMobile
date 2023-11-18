package com.example.moontech.ui.viewmodel.dataclasses

import com.example.moontech.data.dataclasses.RoomData
import kotlinx.coroutines.flow.StateFlow

interface WatchedRoomsController {
    val watchedRooms: StateFlow<List<RoomData>>
    fun addWatchedRoom(code: String, password: String)
    fun removeWatchedRoom(code: String)
}