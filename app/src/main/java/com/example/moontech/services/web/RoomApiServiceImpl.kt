package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.ManagedRoomWithCameras
import com.example.moontech.data.dataclasses.RoomCreationRequest
import com.example.moontech.data.dataclasses.RoomCreationResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class RoomApiServiceImpl(private val httpClient: HttpClient): RoomApiService {
    companion object {
        private const val endpoint = "/room"
    }
    override suspend fun createRoom(roomRequest: RoomCreationRequest): Result<RoomCreationResponse> {
        return httpClient.postResult(endpoint) {
            setBody(roomRequest)
        }
    }

    override suspend fun getUserRooms(): Result<List<ManagedRoomWithCameras>> {
        return httpClient.getResult(endpoint)
    }

    override suspend fun deleteRoom(code: String): Result<Boolean> {
        return httpClient.deleteWithStatus("$endpoint/$code")
    }
}