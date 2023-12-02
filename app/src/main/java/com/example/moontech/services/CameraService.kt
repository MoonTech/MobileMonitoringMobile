package com.example.moontech.services

import androidx.camera.core.Preview
import com.example.moontech.data.dataclasses.AppError
import kotlinx.coroutines.flow.StateFlow

interface CameraService {
    val isStreaming: StateFlow<Boolean>
    val isPreview: StateFlow<Boolean>
    val streamError: StateFlow<AppError>
    fun startStream(url: String)

    fun stopStream()

    fun startPreview(surfaceProvider: Preview.SurfaceProvider)

    fun stopPreview()

    fun closeServiceIfNotUsed()
}