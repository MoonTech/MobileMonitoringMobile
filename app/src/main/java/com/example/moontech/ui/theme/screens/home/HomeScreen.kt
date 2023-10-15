package com.example.moontech.ui.theme.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moontech.R
import com.example.moontech.ui.theme.components.CenterColumn
import com.example.moontech.ui.theme.components.PrimaryButton

@Composable
fun HomeScreen(
    onCreateRoom: () -> Unit,
    onAddCamera: () -> Unit,
    onWatchTransmission: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterColumn(modifier = modifier) {
        CenterColumn(modifier = Modifier.widthIn(max = 300.dp)) {
            PrimaryButton(
                text = stringResource(R.string.create_room),
                modifier = buttonModifier,
                onClick = onCreateRoom
            )
            PrimaryButton(
                text = stringResource(R.string.add_camera),
                modifier = buttonModifier,
                onClick = onAddCamera
            )
            PrimaryButton(
                text = stringResource(R.string.watch_transmission),
                modifier = buttonModifier,
                onClick = onWatchTransmission
            )
        }
    }
}

private val buttonModifier = Modifier
    .fillMaxWidth()
    .padding(2.dp)

@Preview
@Composable
fun PreviewHomeScreen() {
    Surface() {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            onAddCamera = {},
            onCreateRoom = {},
            onWatchTransmission = {}
        )
    }
}