package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class UserRoomsResponse(val rooms: List<ManagedRoom>) {
}