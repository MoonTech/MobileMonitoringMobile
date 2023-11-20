package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.ManagedRoomWithCameras
import com.example.moontech.data.dataclasses.RoomCreationRequest
import com.example.moontech.data.dataclasses.RoomCreationResponse

interface RoomApiService {
    suspend fun createRoom(roomRequest: RoomCreationRequest): Result<RoomCreationResponse>

    suspend fun getUserRooms(): Result<List<ManagedRoomWithCameras>>

    suspend fun deleteRoom(code: String): Result<Boolean>
}