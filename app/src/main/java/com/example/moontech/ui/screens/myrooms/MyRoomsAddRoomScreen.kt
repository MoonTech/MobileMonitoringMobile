package com.example.moontech.ui.screens.myrooms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.R
import com.example.moontech.ui.screens.base.AuthScreenBase

@Composable
fun MyRoomsAddRoomScreen(
    onAddRoom: (code: String, password: String?) -> Unit,
    modifier: Modifier = Modifier
) = AuthScreenBase(
    firstButtonLabel = R.string.create,
    firstTextFieldLabel = R.string.room_name,
    secondTextFieldLabel = R.string.password,
    screenLabel = R.string.create_room,
    firstButtonAction = onAddRoom,
    modifier = modifier
)