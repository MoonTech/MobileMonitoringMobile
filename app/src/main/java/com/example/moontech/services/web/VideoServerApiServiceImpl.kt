package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.RecordRequest
import com.example.moontech.data.dataclasses.StreamRequest
import com.example.moontech.data.dataclasses.StreamResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody

class VideoServerApiServiceImpl(private val httpClient: HttpClient): VideoServerApiService {
    companion object {
        private const val endpoint = "/videoServer"
    }
    override suspend fun stream(streamRequest: StreamRequest): Result<StreamResponse> {
        return httpClient.postResult("$endpoint/streamUrl") {
            setBody(streamRequest)
        }
    }

    override suspend fun startRecord(request: RecordRequest): Result<Boolean> {
        return httpClient.putWithStatus("$endpoint/record/start") {
            setBody(request)
        }
    }

    override suspend fun stopRecord(request: RecordRequest): Result<Boolean> {
        return httpClient.putWithStatus("$endpoint/record/stop") {
            setBody(request)
        }
    }

    override suspend fun checkRecord(request: RecordRequest): Result<Boolean> {
        return httpClient.getWithStatus("$endpoint/record/check") {
            parameter("id", request.cameraId)
        }
    }
}