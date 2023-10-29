package com.example.moontech.ui.screens.roomlogin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moontech.R
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.SingleValueAndConfirmComponent
import com.example.moontech.ui.viewmodel.AppViewModel
import com.example.moontech.ui.viewmodel.dataclasses.Room

@Composable
fun RoomLoginScreen(
    viewModel: AppViewModel,
    onRoomLoggedIn: () -> Unit,
    modifier: Modifier = Modifier,
    onConfirm: (roomCode: String, password: String?) -> Unit,
    requiredPrivilege: Room.() -> Boolean
) = CenterScreen(modifier = modifier) {
    val uiState by viewModel.uiState.collectAsState()
    var tRoomCode by rememberSaveable { mutableStateOf("") }
    var roomCode by rememberSaveable { mutableStateOf("") }
    var roomPassword: String? by rememberSaveable { mutableStateOf(null) }
    val room: Room? = uiState.rooms[roomCode]
    CenterColumn(modifier = Modifier.widthIn(max = 300.dp)) {
        when {
            room == null -> {
                SingleValueAndConfirmComponent(
                    value = tRoomCode,
                    onValueChanged = { tRoomCode = it },
                    confirm = {
                        onConfirm(tRoomCode, roomPassword)
                        roomCode = tRoomCode
                    },
                    valueLabel = R.string.room_code,
                    confirmLabel = R.string.enter
                )
            }

            !room.requiredPrivilege() -> {
                SingleValueAndConfirmComponent(
                    value = roomPassword,
                    onValueChanged = { roomPassword = it },
                    confirm = { onConfirm(roomCode, roomPassword) },
                    valueLabel = R.string.room_password,
                    confirmLabel = R.string.enter
                )
            }

            else -> {
                onRoomLoggedIn()
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        RoomLoginScreen(
            onRoomLoggedIn = {},
            viewModel = viewModel(),
            requiredPrivilege = { false },
            onConfirm = { _, _ -> })
    }
}