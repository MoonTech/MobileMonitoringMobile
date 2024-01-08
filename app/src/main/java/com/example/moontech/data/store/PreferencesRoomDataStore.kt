package com.example.moontech.data.store

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.moontech.data.dataclasses.RoomData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesRoomDataStore(
    private val dataStore: DataStore<Preferences>
) : RoomDataStore {

    companion object {
        private const val TAG = "PreferencesRoomDataStore"
        private val ROOM_DATA_KEY = stringSetPreferencesKey("ROOM_DATA_KEY")
    }

    override val rooms: Flow<List<RoomData>> = dataStore.data
        .catching()
        .map { preferences ->
            preferences[ROOM_DATA_KEY]?.map {
                Json.decodeFromString<RoomData>(it)
            }?.toList() ?: listOf()
        }

    override suspend fun add(roomData: RoomData) {
        Log.i(TAG, "addRoomData: begin")
        val encodedRoomData = Json.encodeToString(roomData)
        dataStore.editStringValues(ROOM_DATA_KEY) { rooms -> rooms.apply { add(encodedRoomData) } }
    }

    override suspend fun delete(code: String) {
        dataStore.editStringValues(ROOM_DATA_KEY) { rooms ->
            rooms.apply {
                removeIf {
                    Json.decodeFromString<RoomData>(it).code == code
                }
            }
        }
    }
}
