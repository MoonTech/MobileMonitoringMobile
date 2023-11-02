package com.example.moontech.services

import androidx.camera.core.Preview

interface CameraService {
    fun startStream(rtmpUrl: String)

    fun stopStream()

    fun startPreview(surfaceProvider: Preview.SurfaceProvider)

    fun stopPreview()
}