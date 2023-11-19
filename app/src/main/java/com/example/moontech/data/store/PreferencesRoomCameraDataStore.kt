package com.example.moontech.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import catching
import com.example.moontech.data.dataclasses.RoomCamera
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesRoomCameraDataStore(private val dataStore: DataStore<Preferences>) : RoomCameraDataStore {
    companion object {
        private const val TAG = "PreferencesCameraDataStore"
        private val CAMERA_DATA_KEY = stringPreferencesKey("camera_data_key")
    }
    override val roomCamera: Flow<RoomCamera?> = dataStore.data
        .catching()
        .map { preferences ->
            preferences[CAMERA_DATA_KEY]?.let { Json.decodeFromString(it) }
        }
    override suspend fun saveCamera(roomCamera: RoomCamera) {
        dataStore.edit { preferences ->
            preferences[CAMERA_DATA_KEY] = Json.encodeToString(roomCamera)
        }
    }

    override suspend fun deleteCamera() {
        dataStore.edit { preferences ->
            preferences.remove(CAMERA_DATA_KEY)
        }
    }
}