package com.example.moontech.ui.viewmodel

import com.example.moontech.ui.viewmodel.dataclasses.Room

data class UiState(
    val rooms: Map<String, Room> = mapOf(),
    val watchedRoom: String? = null,
    val transmittingRoom: String? = null
) {
    fun isWatching(): Boolean = watchedRoom != null
}