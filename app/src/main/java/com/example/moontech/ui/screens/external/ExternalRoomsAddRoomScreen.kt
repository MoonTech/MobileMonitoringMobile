package com.example.moontech.ui.screens.external

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.R
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.ui.screens.base.AuthScreenBase

@Composable
fun ExternalRoomsAddRoomScreen(
    addRoom: (code: String, password: String) -> Unit,
    emitError: (error: AppState.Error) -> Unit,
    modifier: Modifier = Modifier,
    onAddWithQrCode: () -> Unit
) = AuthScreenBase(
    modifier = modifier,
    firstButtonLabel = R.string.add,
    firstTextFieldLabel = R.string.room_name,
    secondTextFieldLabel = R.string.password,
    screenLabel = R.string.add_room,
    firstButtonAction = addRoom,
    emitError = emitError,
    secondButtonLabel = R.string.add_qr_code,
    secondButtonAction = onAddWithQrCode
)
