package com.example.moontech.application

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.moontech.data.repository.InMemoryRoomRepository
import com.example.moontech.data.repository.InMemoryUserRepository
import com.example.moontech.data.repository.RoomRepository
import com.example.moontech.data.repository.UserRepository
import com.example.moontech.data.store.PreferencesRoomDataStore
import com.example.moontech.data.store.PreferencesUserDataStore
import com.example.moontech.data.store.RoomDataStore
import com.example.moontech.data.store.UserDataStore

interface AppContainer {
    val userRepository: UserRepository
    val userDataStore: UserDataStore
    val roomRepository: RoomRepository
    val roomDataStore: RoomDataStore
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "preferences"
)

class DefaultAppContainer(context: Context) : AppContainer {
    override val userRepository: UserRepository by lazy {
        InMemoryUserRepository()
    }
    override val userDataStore: UserDataStore by lazy {
        PreferencesUserDataStore(context.dataStore)
    }
    override val roomRepository: RoomRepository by lazy {
        InMemoryRoomRepository()
    }
    override val roomDataStore: RoomDataStore by lazy {
        PreferencesRoomDataStore(context.dataStore)
    }
}