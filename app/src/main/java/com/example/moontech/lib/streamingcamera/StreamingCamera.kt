package com.example.moontech.lib.streamingcamera

import androidx.camera.core.Preview

interface StreamingCamera {
    fun startStream(rtmpUrl: String)

    fun stopStream()

    fun startPreview(): Preview

    fun stopPreview()
}