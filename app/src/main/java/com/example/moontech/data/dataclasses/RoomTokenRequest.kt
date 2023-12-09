package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class RoomTokenRequest(val roomName: String, val password: String)
