package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class WatchRequest(
    val roomName: String,
    val password: String?
)