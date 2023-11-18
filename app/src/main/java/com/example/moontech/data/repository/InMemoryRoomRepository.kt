package com.example.moontech.data.repository

import android.util.Log
import com.example.moontech.data.dataclasses.Result
import com.example.moontech.ui.viewmodel.dataclasses.Room
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InMemoryRoomRepository : RoomRepository {
    companion object {
        private const val TAG = "InMemoryRoomRepository"
        private val testRooms = mutableListOf(
            Room(code = "My room 1"),
            Room(code = "My room 2"),
            Room(code = "My room 3"),
            Room(code = "My room 4")
        )
    }

    override suspend fun getRooms(): Flow<Result<List<Room>>> = flow {
        Log.d(TAG, "getRooms: fetching rooms")
        emit(Result.Loading())
        delay(1000)
        emit(Result.Success(testRooms))
    }

    override suspend fun addRoom(code: String, password: String?) {
        testRooms.add(Room(code = code))
    }
}