package com.example.moontech.ui.viewmodel

import com.example.moontech.data.dataclasses.Room

data class UiState(
    val rooms: Map<String, Room> = mapOf(),
    private val watchedRoomCode: String? = null,
    private val transmittingRoomCode: String? = null
) {
    val watchedRoom: Room?
        get() = rooms[watchedRoomCode]

    val transmittingRoom: Room?
        get() = rooms[transmittingRoomCode]

}