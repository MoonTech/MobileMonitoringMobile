package com.example.moontech.services

import androidx.camera.core.Preview

interface CameraService {
    val isStreaming: Boolean
    val isPreview: Boolean
    fun startStream(url: String)

    fun stopStream()

    fun startPreview(surfaceProvider: Preview.SurfaceProvider)

    fun stopPreview()

    fun closeServiceIfNotUsed()
}