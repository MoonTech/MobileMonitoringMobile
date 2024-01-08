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
import io.ktor.client.request.cookie
import io.ktor.client.request.setBody

class RoomApiServiceImpl(private val httpClient: HttpClient) : RoomApiService {
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

    override suspend fun watchRoom(
        request: WatchRequest,
        accessToken: String?
    ): Result<WatchedRoom> {
        if (accessToken == null) {
            return httpClient.postResult("$endpoint/watch") {
                setBody(request)
            }
        }
        val tokenResponse =
            httpClient.postResult<RoomTokenResponse>("$endpoint/refreshToken/${request.roomName}") {
                this.headers.append("Authorization", "Bearer $accessToken")
            }
        tokenResponse.onSuccess {
            return httpClient.postResult("$endpoint/watch") {
                setBody(request)
                this.headers.append("Authorization", "Bearer ${it.accessToken}")
            }
        }
        return Result.failure(Exception("Access denied: unauthorized"))
    }

    override suspend fun refreshWatchCookie(
        code: String, accessToken: String, refreshToken: String
    ): Result<RoomTokenResponse> {
        return httpClient.postResult("$endpoint/refreshToken/${code}") {
            this.headers.append("Authorization", "Bearer $accessToken")
            this.cookie("refreshToken", refreshToken)
        }
    }

    override suspend fun getRoomToken(request: RoomTokenRequest): Result<RoomTokenResponse> {
        return httpClient.postResult("$endpoint/token") {
            setBody(request)
        }
    }

    override suspend fun getRecordings(
        code: String,
        accessToken: String?
    ): Result<RecordingsResponse> {
        if (accessToken == null) {
            return httpClient.getResult("$endpoint/recordings/$code")
        }
        httpClient.postResult<RoomTokenResponse>("$endpoint/refreshToken/$code") {
            this.headers.append("Authorization", "Bearer $accessToken")
        }.onSuccess {
            return httpClient.getResult("$endpoint/recordings/$code") {
                this.headers.append("Authorization", "Bearer ${it.accessToken}")
            }
        }
        return Result.failure(Exception("Not authorized"))
    }
}