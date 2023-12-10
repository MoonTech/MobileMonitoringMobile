package com.example.moontech.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moontech.application.MoontechApplication

object AppViewModelFactoryProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MoontechApplication
            AppViewModel(
                application = application,
                userDataStore = application.container.userDataStore,
                roomDataStore = application.container.roomDataStore,
                roomCameraDataStore = application.container.roomCameraDataStore,
                userApiService = application.container.userApiService,
                roomApiService = application.container.roomApiService,
                cameraApiService = application.container.cameraApiService,
                videoServerApiService = application.container.videoServerApiService,
                httpClient = application.container.httpClient
            )
        }
    }
}