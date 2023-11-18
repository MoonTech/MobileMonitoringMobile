package com.example.moontech.ui.screens.transmit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.R
import com.example.moontech.ui.screens.base.AuthScreenBase

@Composable
fun TransmitAddRoomScreen(
    addRoomCamera: (code: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) = AuthScreenBase(
    firstButtonLabel = R.string.add,
    firstTextFieldLabel = R.string.room_name,
    secondTextFieldLabel = R.string.password,
    screenLabel = R.string.add_camera,
    firstButtonAction = addRoomCamera,
    modifier = modifier
)