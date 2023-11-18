package com.example.moontech.data.store

import com.example.moontech.data.dataclasses.RoomData
import kotlinx.coroutines.flow.Flow

interface RoomDataStore {
    val rooms: Flow<List<RoomData>>
    suspend fun addRoomData(roomData: RoomData)
    suspend fun removeRoomData(roomData: RoomData)
}