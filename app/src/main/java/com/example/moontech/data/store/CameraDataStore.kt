package com.example.moontech.data.store

import com.example.moontech.data.dataclasses.Camera
import kotlinx.coroutines.flow.Flow

interface CameraDataStore {
    val camera: Flow<Camera?>
    suspend fun saveCamera(camera: Camera)
    suspend fun deleteCamera()
}