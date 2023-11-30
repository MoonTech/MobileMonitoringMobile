package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class WatchedRoomCamera(
    val id: String,
    val acceptationState: Boolean?
) {
}