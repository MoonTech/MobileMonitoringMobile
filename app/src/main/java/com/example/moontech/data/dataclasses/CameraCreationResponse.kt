package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class CameraCreationResponse(val cameraToken: String, val cameraUrl: String)