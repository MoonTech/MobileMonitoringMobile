package com.example.moontech.ui.viewmodel

import com.example.moontech.data.dataclasses.RoomCamera
import kotlinx.coroutines.flow.StateFlow

interface CameraController {
    val externalRoomCameras: StateFlow<List<RoomCamera>>
    val myRoomCameras: StateFlow<List<RoomCamera>>
    fun removeRoomCamera(roomCamera: RoomCamera)
}