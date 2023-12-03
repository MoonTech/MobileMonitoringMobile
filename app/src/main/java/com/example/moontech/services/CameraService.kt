package com.example.moontech.services

import androidx.camera.core.Preview
import kotlinx.coroutines.flow.Flow

interface CameraService {
    val serviceState: Flow<CameraServiceState>
    fun startStream(url: String, name: String)

    fun stopStream()

    fun startPreview(surfaceProvider: Preview.SurfaceProvider)

    fun stopPreview()

    fun closeServiceIfNotUsed()
}