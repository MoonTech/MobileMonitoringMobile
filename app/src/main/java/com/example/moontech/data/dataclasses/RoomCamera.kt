package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class RoomCamera(
    val roomCode: String,
    val token: String
)
