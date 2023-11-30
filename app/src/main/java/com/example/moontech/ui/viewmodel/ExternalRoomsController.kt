package com.example.moontech.ui.viewmodel

import com.example.moontech.data.dataclasses.RoomData
import kotlinx.coroutines.flow.StateFlow

interface ExternalRoomsController {
    val externalRooms: StateFlow<List<RoomData>>
    fun addExternalRoom(code: String, password: String)
    fun removeExternalRoom(code: String)
}