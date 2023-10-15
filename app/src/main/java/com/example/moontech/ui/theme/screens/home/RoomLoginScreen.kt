package com.example.moontech.ui.theme.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moontech.R
import com.example.moontech.ui.theme.components.CenterColumn
import com.example.moontech.ui.theme.components.PrimaryButton

@Composable
fun RoomLoginScreen(modifier: Modifier = Modifier) {
    var roomCode by rememberSaveable { mutableStateOf("") }
    CenterColumn(modifier = modifier) {
        CenterColumn(modifier = Modifier.widthIn(max = 300.dp)) {
            TextField(
                value = roomCode,
                onValueChange = { roomCode = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                label = { Text(text = stringResource(R.string.room_code)) })
            PrimaryButton(
                text = stringResource(R.string.enter),
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        RoomLoginScreen()
    }
}