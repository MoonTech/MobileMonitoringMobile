package com.example.moontech.lib.streamingcamera

import androidx.camera.core.Preview.SurfaceProvider

interface StreamingCamera {
    fun startStream(rtmpUrl: String)

    fun stopStream()

    fun startPreview(surfaceProvider: SurfaceProvider)

    fun stopPreview()
}