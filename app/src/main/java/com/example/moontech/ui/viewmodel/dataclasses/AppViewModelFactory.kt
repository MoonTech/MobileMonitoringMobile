package com.example.moontech.ui.viewmodel.dataclasses

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moontech.application.MoontechApplication
import com.example.moontech.ui.viewmodel.AppViewModel

object AppViewModelFactoryProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MoontechApplication
            AppViewModel(
                application,
                application.container.userRepository,
                application.container.userDataStore
            )
        }
    }
}