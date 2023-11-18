package com.example.moontech.data.repository

import com.example.moontech.data.dataclasses.Result
import com.example.moontech.ui.viewmodel.dataclasses.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun getRooms(): Flow<Result<List<Room>>>
    suspend fun addRoom(code: String, password: String?)
}