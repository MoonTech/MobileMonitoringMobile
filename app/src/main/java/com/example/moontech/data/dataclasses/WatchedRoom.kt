package com.example.moontech.data.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchedRoom(
    @SerialName("roomName")
    override val code: String,
    val connectedCameras: List<WatchedRoomCamera>
) : ObjectWithRoomCode {

}