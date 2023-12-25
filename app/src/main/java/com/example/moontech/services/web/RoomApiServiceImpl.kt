package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.RecordingsResponse
import com.example.moontech.data.dataclasses.RoomCreationRequest
import com.example.moontech.data.dataclasses.RoomCreationResponse
import com.example.moontech.data.dataclasses.RoomTokenRequest
import com.example.moontech.data.dataclasses.RoomTokenResponse
import com.example.moontech.data.dataclasses.UserRoomsResponse
import com.example.moontech.data.dataclasses.WatchRequest
import com.example.moontech.data.dataclasses.WatchedRoom
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

    override suspend fun getUserRooms(): Result<UserRoomsResponse> {
        return httpClient.getResult(endpoint)
    }

    override suspend fun deleteRoom(code: String): Result<Boolean> {
        return httpClient.deleteWithStatus("$endpoint/$code")
    }

    override suspend fun watchRoom(request: WatchRequest, accessToken: String?): Result<WatchedRoom> {
        return httpClient.postResult("$endpoint/watch") {
            setBody(request)
            accessToken?.let {
                this.headers.append("Authorization", "Bearer $accessToken")
            }
        }
    }

    override suspend fun getRoomToken(request: RoomTokenRequest): Result<RoomTokenResponse> {
        return httpClient.postResult("$endpoint/token") {
            setBody(request)
        }
    }

    override suspend fun getRecordings(code: String): Result<RecordingsResponse> {
        return httpClient.getResult("$endpoint/recordings/$code")
    }
}