package com.example.moontech.services.web

import com.example.moontech.data.dataclasses.CameraCreationResponse
import com.example.moontech.data.dataclasses.CameraRequest

interface CameraApiService {
    suspend fun addCamera(cameraRequest: CameraRequest): Result<CameraCreationResponse>
    suspend fun deleteCamera(id: String): Result<Boolean>
    suspend fun acceptCamera(id: String): Result<Boolean>
}