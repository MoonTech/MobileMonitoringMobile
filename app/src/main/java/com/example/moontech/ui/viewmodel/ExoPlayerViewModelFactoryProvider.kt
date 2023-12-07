package com.example.moontech.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moontech.application.MoontechApplication

object ExoPlayerViewModelFactoryProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MoontechApplication
            ExoPlayerViewModel(application)
        }
    }
}