package com.example.moontech.data.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomCreationRequest(
    @SerialName("name")
    override val code: String,
    val password: String
) : ObjectWithRoomCode {
}