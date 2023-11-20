package com.example.moontech.data.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomCreationResponse(@SerialName("roomName") override val code: String): ObjectWithRoomCode {
}