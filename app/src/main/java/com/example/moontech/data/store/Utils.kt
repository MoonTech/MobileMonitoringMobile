package com.example.moontech.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit


suspend fun DataStore<Preferences>.editStringValues(
    key: Preferences.Key<Set<String>>,
    block: (MutableSet<String>) -> Set<String>
) {
    edit { preferences: MutablePreferences ->
        val roomDataSet = preferences[key]?.toMutableSet() ?: mutableSetOf()
        preferences[key] = block(roomDataSet)
    }
}
