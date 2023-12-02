package com.example.moontech.ui.viewmodel

import com.example.moontech.data.dataclasses.WatchedRoom
import kotlinx.coroutines.flow.StateFlow

interface WatchController {
    val watchedRoom: StateFlow<WatchedRoom?>
}