package com.example.moontech.data.store

import com.example.moontech.data.dataclasses.RoomCamera
import kotlinx.coroutines.flow.Flow

interface RoomCameraDataStore {
    val roomCameras: Flow<List<RoomCamera>>
    suspend fun add(roomCamera: RoomCamera)
    suspend fun delete(roomCamera: RoomCamera)
}