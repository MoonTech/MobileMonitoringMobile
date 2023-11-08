package com.example.moontech.services

import androidx.camera.core.Preview
import kotlinx.coroutines.flow.StateFlow

interface CameraService {
    val isStreaming: StateFlow<Boolean>
    val isPreview: StateFlow<Boolean>
    fun startStream(url: String)

    fun stopStream()

    fun startPreview(surfaceProvider: Preview.SurfaceProvider)

    fun stopPreview()

    fun closeServiceIfNotUsed()
}