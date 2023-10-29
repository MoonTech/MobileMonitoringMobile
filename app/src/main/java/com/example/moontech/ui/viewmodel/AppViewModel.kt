package com.example.moontech.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moontech.ui.viewmodel.dataclasses.Room
import com.example.moontech.ui.viewmodel.dataclasses.RoomPrivilege
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loginToRoomForWatching(roomCode: String, password: String? = null) {
        if (password != null) {
            val room = Room(roomCode, RoomPrivilege.Watch.code)
            addOrUpdateARoom(roomCode, room)
            updateWatchedRoom(room)
        } else {
            addOrUpdateARoom(roomCode, Room(roomCode))
        }
    }

    fun loginToRoomForTransmitting(roomCode: String, password: String? = null) {
        if (password != null) {
            val room = Room(roomCode, RoomPrivilege.Transmit.code)
            addOrUpdateARoom(roomCode, room)
            updateTransmittingRoom(room)
        } else {
            addOrUpdateARoom(roomCode, Room(roomCode))
        }
    }

    private fun addOrUpdateARoom(roomCode: String, room: Room) {
        _uiState.update { prevState ->
            prevState.copy(rooms = prevState.rooms.toMutableMap().apply {
                val roomToPut = get(roomCode)?.merge(room) ?: room
                put(roomCode, roomToPut)
            })
        }
    }

    private fun updateWatchedRoom(room: Room) {
        _uiState.update { prevState ->
            prevState.copy(watchedRoomCode = room.code)
        }
    }

    private fun updateTransmittingRoom(room: Room) {
        _uiState.update { prevState ->
            prevState.copy(transmittingRoomCode = room.code)
        }
    }
}