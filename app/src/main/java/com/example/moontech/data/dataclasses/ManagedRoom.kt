package com.example.moontech.data.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ManagedRoom(
    @SerialName("roomName") override val code: String,
    val cameras: List<ManagedRoomCamera>
) : ObjectWithRoomCode {
}