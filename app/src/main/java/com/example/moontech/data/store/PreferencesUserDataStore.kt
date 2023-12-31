package com.example.moontech.data.store

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import catching
import com.example.moontech.data.dataclasses.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesUserDataStore(private val dataStore: DataStore<Preferences>) : UserDataStore {
    companion object {
        private const val TAG = "InMemoryUserDataStore"
        private val USER_DATA_KEY = stringPreferencesKey("user_data_key")
    }

    override val userData: Flow<UserData?> = dataStore.data
        .catching()
        .map { preferences ->
            Log.i(TAG, "fetching ${preferences[USER_DATA_KEY]}")
            preferences[USER_DATA_KEY]?.let { Json.decodeFromString<UserData>(it) }
        }

    override suspend fun save(userData: UserData) {
        dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = Json.encodeToString(userData)
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.minusAssign(USER_DATA_KEY)
        }
    }
}