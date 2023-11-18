package com.example.moontech.ui.viewmodel

import com.example.moontech.data.dataclasses.Result
import com.example.moontech.data.dataclasses.RoomCamera
import kotlinx.coroutines.flow.StateFlow

interface CameraController {
    val roomCamera: StateFlow<Result<RoomCamera>>
    fun addRoomCamera(code: String, password: String)
    fun removeRoomCamera()
}