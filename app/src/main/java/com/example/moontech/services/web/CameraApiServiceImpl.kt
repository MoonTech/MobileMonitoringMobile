package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.CameraCreationResponse
import com.example.moontech.data.dataclasses.CameraRequest
import com.example.moontech.data.dataclasses.StreamResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class CameraApiServiceImpl(private val httpClient: HttpClient): CameraApiService {
    companion object {
        private const val endpoint = "/camera"
    }
    override suspend fun addCamera(cameraRequest: CameraRequest): Result<CameraCreationResponse> {
        return httpClient.postResult(endpoint) {
            setBody(cameraRequest)
        }
    }

    override suspend fun deleteCamera(id: String): Result<Boolean> {
        return httpClient.deleteWithStatus("$endpoint/$id")
    }

    override suspend fun acceptCamera(id: String): Result<Boolean> {
        return httpClient.putWithStatus("$endpoint/$id")
    }

    override suspend fun stream(cameraRequest: CameraRequest): Result<StreamResponse> {
        return httpClient.postResult("/videoServer/streamUrl") {
            setBody(cameraRequest)
        }
    }

}