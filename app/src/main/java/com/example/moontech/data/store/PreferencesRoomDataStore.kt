package com.example.moontech.data.store

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.moontech.data.dataclasses.RoomData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

class PreferencesRoomDataStore(
    private val dataStore: DataStore<Preferences>
) : RoomDataStore {

    companion object {
        private const val TAG = "PreferencesRoomDataStore"
        private val ROOM_DATA_KEY = stringSetPreferencesKey("ROOM_DATA_KEY")
    }

    override val rooms: Flow<List<RoomData>> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emptyPreferences()
            }
            throw it
        }.map { preferences ->
            preferences[ROOM_DATA_KEY]?.map {
                Json.decodeFromString<RoomData>(it)
            }?.toList() ?: listOf()
        }

    override suspend fun addRoomData(roomData: RoomData) {
        Log.i(TAG, "addRoomData: begin")
        val encodedRoomData = Json.encodeToString(roomData)
        dataStore.editStringValues { rooms -> rooms.apply { add(encodedRoomData) } }
    }

    override suspend fun removeRoomData(roomData: RoomData) {
        val encodedRoomData = Json.encodeToString(roomData)
        dataStore.editStringValues { rooms -> rooms.apply { remove(encodedRoomData) } }
    }

    private suspend fun DataStore<Preferences>.editStringValues(block: (MutableSet<String>) -> Set<String>) {
        edit { preferences: MutablePreferences ->
            val roomDataSet = preferences[ROOM_DATA_KEY]?.toMutableSet() ?: mutableSetOf()
            preferences[ROOM_DATA_KEY] = block(roomDataSet)
        }
    }
}