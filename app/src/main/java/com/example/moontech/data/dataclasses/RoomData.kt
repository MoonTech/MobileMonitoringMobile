package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class RoomData(
    override val code: String,
    val password: String,
    val authToken:String = ""
): ObjectWithRoomCode