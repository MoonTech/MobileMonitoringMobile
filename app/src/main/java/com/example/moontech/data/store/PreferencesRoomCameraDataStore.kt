package com.example.moontech.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import catching
import com.example.moontech.data.dataclasses.RoomCamera
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesRoomCameraDataStore(private val dataStore: DataStore<Preferences>) :
    RoomCameraDataStore {
    companion object {
        private const val TAG = "PreferencesCameraDataStore"
        private val CAMERA_DATA_KEY = stringSetPreferencesKey("camera_data_key")
    }

    override val roomCameras: Flow<List<RoomCamera>> = dataStore.data
        .catching()
        .map { preferences ->
            preferences[CAMERA_DATA_KEY]?.map {
                Json.decodeFromString<RoomCamera>(it)
            }?.toList() ?: listOf()
        }

    override suspend fun add(roomCamera: RoomCamera) {
        val encodedRoomCamera = Json.encodeToString(roomCamera);
        dataStore.editStringValues(CAMERA_DATA_KEY) { it.apply { add(encodedRoomCamera) } }
    }

    override suspend fun delete(roomCamera: RoomCamera) {
        val encodedRoomCamera = Json.encodeToString(roomCamera);
        dataStore.editStringValues(CAMERA_DATA_KEY) { it.apply { remove(encodedRoomCamera) } }
    }
}