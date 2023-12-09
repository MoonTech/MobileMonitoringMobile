package com.example.moontech.ui.viewmodel

import com.example.moontech.data.dataclasses.RoomCamera
import com.example.moontech.ui.screens.common.RoomType
import kotlinx.coroutines.flow.StateFlow

interface CameraController {
    val externalRoomCameras: StateFlow<List<RoomCamera>>
    val roomCameras: StateFlow<List<RoomCamera>>
    val myRoomCameras: StateFlow<List<RoomCamera>>
    fun removeRoomCamera(roomCamera: RoomCamera)
    fun addRoomCamera(
        cameraName: String,
        roomCode: String,
        password: String?,
        roomType: RoomType,
        onSuccess: () -> Unit
    )
}