package com.example.moontech.data.dataclasses

import com.example.moontech.ui.screens.common.RoomType
import kotlinx.serialization.Serializable

@Serializable
data class RoomCamera(
    override val code: String,
    val token: String,
    val url: String,
    val roomType: RoomType
): ObjectWithRoomCode
