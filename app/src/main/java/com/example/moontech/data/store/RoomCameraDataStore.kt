package com.example.moontech.data.store

import com.example.moontech.data.dataclasses.RoomCamera
import kotlinx.coroutines.flow.Flow

interface RoomCameraDataStore {
    val roomCamera: Flow<RoomCamera?>
    suspend fun saveCamera(roomCamera: RoomCamera)
    suspend fun deleteCamera()
}