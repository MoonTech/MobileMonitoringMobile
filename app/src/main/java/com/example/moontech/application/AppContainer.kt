package com.example.moontech.application

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.moontech.R
import com.example.moontech.data.repository.InMemoryRoomRepository
import com.example.moontech.data.repository.InMemoryUserRepository
import com.example.moontech.data.repository.RoomRepository
import com.example.moontech.data.repository.UserRepository
import com.example.moontech.data.store.PreferencesRoomCameraDataStore
import com.example.moontech.data.store.PreferencesRoomDataStore
import com.example.moontech.data.store.PreferencesUserDataStore
import com.example.moontech.data.store.RoomCameraDataStore
import com.example.moontech.data.store.RoomDataStore
import com.example.moontech.data.store.UserDataStore
import com.example.moontech.services.web.CameraApiService
import com.example.moontech.services.web.CameraApiServiceImpl
import com.example.moontech.services.web.HttpClientFactory
import com.example.moontech.services.web.RoomApiService
import com.example.moontech.services.web.RoomApiServiceImpl
import com.example.moontech.services.web.TokenManager
import com.example.moontech.services.web.UserApiService
import com.example.moontech.services.web.UserApiServiceImpl
import com.example.moontech.services.web.VideoServerApiService
import com.example.moontech.services.web.VideoServerApiServiceImpl
import io.ktor.client.HttpClient

interface AppContainer {
    val userRepository: UserRepository
    val userDataStore: UserDataStore
    val roomRepository: RoomRepository
    val roomDataStore: RoomDataStore
    val roomCameraDataStore: RoomCameraDataStore
    val httpClient: HttpClient
    val userApiService: UserApiService
    val roomApiService: RoomApiService
    val cameraApiService: CameraApiService
    val videoServerApiService: VideoServerApiService
    val tokenManager: TokenManager
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
    override val roomCameraDataStore: RoomCameraDataStore by lazy {
        PreferencesRoomCameraDataStore(context.dataStore)
    }
    override val httpClient: HttpClient by lazy {
        val baseUrl = context.getString(R.string.base_url)
        HttpClientFactory.create(baseUrl, tokenManager)
    }
    override val userApiService: UserApiService by lazy {
        UserApiServiceImpl(httpClient)
    }
    override val roomApiService: RoomApiService by lazy {
        RoomApiServiceImpl(httpClient)
    }
    override val cameraApiService: CameraApiService by lazy {
        CameraApiServiceImpl(httpClient)
    }
    override val tokenManager: TokenManager by lazy {
        TokenManager(userDataStore)
    }
    override val videoServerApiService: VideoServerApiService by lazy {
        VideoServerApiServiceImpl(httpClient)
    }
}