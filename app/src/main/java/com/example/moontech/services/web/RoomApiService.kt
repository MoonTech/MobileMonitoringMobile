package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.RoomCreationRequest
import com.example.moontech.data.dataclasses.RoomCreationResponse
import com.example.moontech.data.dataclasses.UserRoomsResponse

interface RoomApiService {
    suspend fun createRoom(roomRequest: RoomCreationRequest): Result<RoomCreationResponse>

    suspend fun getUserRooms(): Result<UserRoomsResponse>

    suspend fun deleteRoom(code: String): Result<Boolean>
}