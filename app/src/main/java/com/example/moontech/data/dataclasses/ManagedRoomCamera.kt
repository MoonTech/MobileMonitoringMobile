package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class ManagedRoomCamera(val id: String, val acceptationState: Boolean) {
}