package com.example.moontech.ui.screens.roomlogin

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moontech.R
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.PrimaryButton
import com.example.moontech.ui.viewmodel.AppViewModel
import com.example.moontech.ui.viewmodel.dataclasses.Room

@Composable
fun RoomLoginScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    var roomCode by rememberSaveable { mutableStateOf("") }
    var roomPassword by rememberSaveable { mutableStateOf("") }
    var room: Room? = uiState.rooms[roomCode]

    CenterColumn(modifier = modifier) {
        CenterColumn(modifier = Modifier.widthIn(max = 300.dp)) {
            when {
                room == null -> {
                    SingleValueAndConfirmComponent(
                        value = roomCode,
                        onValueChanged = { roomCode = it },
                        confirm = { viewModel.loginToRoom(roomCode) },
                        valueLabel = R.string.room_code,
                        confirmLabel = R.string.enter
                    )
                }
                !room.isLoggedIn -> {
                    SingleValueAndConfirmComponent(
                        value = roomPassword,
                        onValueChanged = { roomPassword = it },
                        confirm = { viewModel.loginToRoom(roomCode, roomPassword) },
                        valueLabel = R.string.room_password,
                        confirmLabel = R.string.enter
                    )
                }
                else -> {
                    Text(text = "Logged In")
                }
            }
        }
    }
}

@Composable
fun SingleValueAndConfirmComponent(
    value: String,
    onValueChanged: (value: String) -> Unit,
    confirm: () -> Unit,
    @StringRes valueLabel: Int,
    @StringRes confirmLabel: Int
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        label = { Text(text = stringResource(valueLabel)) })
    PrimaryButton(
        text = stringResource(confirmLabel),
        onClick = confirm,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun Preview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        RoomLoginScreen(viewModel = viewModel())
    }
}