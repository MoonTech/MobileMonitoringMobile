package com.example.moontech.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moontech.ui.viewmodel.dataclasses.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loginToRoom(roomCode: String) {
        addRoom(roomCode, Room(roomCode))
    }

    fun loginToRoom(roomCode: String, password: String) {
        addRoom(roomCode, Room(roomCode, true))
    }

    private fun addRoom(roomCode: String, room: Room) {
        _uiState.update { prevState ->
            prevState.copy(rooms = prevState.rooms.toMutableMap().apply {
                put(roomCode, room)
            })
        }
    }
}