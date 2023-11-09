package com.example.moontech.application

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.moontech.data.repository.InMeMemoryUserRepository
import com.example.moontech.data.repository.UserRepository
import com.example.moontech.data.store.PreferencesUserDataStore
import com.example.moontech.data.store.UserDataStore

interface AppContainer {
    val userRepository: UserRepository
    val userDataStore: UserDataStore
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "preferences"
)

class DefaultAppContainer(context: Context) : AppContainer {
    override val userRepository: UserRepository by lazy {
        InMeMemoryUserRepository()
    }
    override val userDataStore: UserDataStore by lazy {
        PreferencesUserDataStore(context.dataStore)
    }
}