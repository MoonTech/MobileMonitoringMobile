package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.RecordRequest
import com.example.moontech.data.dataclasses.StreamRequest
import com.example.moontech.data.dataclasses.StreamResponse

interface VideoServerApiService {
    suspend fun stream(streamRequest: StreamRequest): Result<StreamResponse>
    suspend fun startRecord(request: RecordRequest): Result<Boolean>
    suspend fun stopRecord(request: RecordRequest, filePrefix: String): Result<String>
    suspend fun checkRecord(request: RecordRequest): Result<Boolean>
}