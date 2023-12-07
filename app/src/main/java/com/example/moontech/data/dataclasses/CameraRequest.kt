package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class CameraRequest(val roomName: String, val password: String?, val cameraName: String)
