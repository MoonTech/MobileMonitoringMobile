package com.example.moontech.ui.viewmodel

import com.example.moontech.ui.viewmodel.dataclasses.Room

data class UiState(
    val rooms: Map<String, Room> = mapOf(),
) {

}