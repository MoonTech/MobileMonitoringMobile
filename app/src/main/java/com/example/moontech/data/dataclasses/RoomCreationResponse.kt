package com.example.moontech.data.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RoomCreationResponse(@SerialName("roomName") override val code: String): ObjectWithRoomCode {
}