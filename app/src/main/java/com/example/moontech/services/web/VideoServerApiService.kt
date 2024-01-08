package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.RecordRequest
import com.example.moontech.data.dataclasses.Recording
import com.example.moontech.data.dataclasses.StreamRequest
import com.example.moontech.data.dataclasses.StreamResponse

interface VideoServerApiService {
    suspend fun stream(streamRequest: StreamRequest): Result<StreamResponse>
    suspend fun startRecord(request: RecordRequest): Result<Boolean>
    suspend fun stopRecord(request: RecordRequest): Result<Boolean>

    /**
     * Returns true if camera specified in request is being recorded.
     * False otherwise.
     * */
    suspend fun checkRecord(request: RecordRequest): Result<Boolean>

    suspend fun downloadRecording(recording: Recording, roomCode: String, accessToken: String?): Result<String>

    suspend fun deleteRecording(recording: Recording): Result<Boolean>
}