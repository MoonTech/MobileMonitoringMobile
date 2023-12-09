package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.RoomCreationRequest
import com.example.moontech.data.dataclasses.RoomCreationResponse
import com.example.moontech.data.dataclasses.RoomTokenRequest
import com.example.moontech.data.dataclasses.RoomTokenResponse
import com.example.moontech.data.dataclasses.UserRoomsResponse
import com.example.moontech.data.dataclasses.WatchRequest
import com.example.moontech.data.dataclasses.WatchedRoom

interface RoomApiService {
    suspend fun createRoom(roomRequest: RoomCreationRequest): Result<RoomCreationResponse>

    suspend fun getUserRooms(): Result<UserRoomsResponse>

    suspend fun deleteRoom(code: String): Result<Boolean>

    suspend fun watchRoom(request: WatchRequest, accessToken: String?): Result<WatchedRoom>

    suspend fun getRoomToken(request: RoomTokenRequest): Result<RoomTokenResponse>
}