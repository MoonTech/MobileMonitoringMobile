package com.example.moontech.data.store

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.moontech.data.dataclasses.RoomCamera
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

class PreferencesRoomCameraDataStore(private val dataStore: DataStore<Preferences>) : RoomCameraDataStore {
    companion object {
        private const val TAG = "PreferencesCameraDataStore"
        private val CAMERA_DATA_KEY = stringPreferencesKey("camera_data_key")
    }
    override val roomCamera: Flow<RoomCamera?> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emptyPreferences()
            }
            throw it
        }.map { preferences ->
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