package com.example.moontech.services

interface CameraService {
    fun startStream(rtmpUrl: String)

    fun stopStream()
}