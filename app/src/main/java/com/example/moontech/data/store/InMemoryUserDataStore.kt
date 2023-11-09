package com.example.moontech.data.store

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.moontech.data.dataclasses.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class InMemoryUserDataStore(private val dataStore: DataStore<Preferences>) : UserDataStore {
    companion object {
        private const val TAG = "InMemoryUserDataStore"
        val USER_DATA_KEY = stringPreferencesKey("user_data_key")
    }

    override val userData: Flow<UserData?> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emptyPreferences()
            }
            throw it
        }.map { preferences ->
            preferences[USER_DATA_KEY]?.let { UserData(it) }
        }

    override suspend fun saveUserData(userData: UserData) {
        dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = userData.token
        }
    }
}