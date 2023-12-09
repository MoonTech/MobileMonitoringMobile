package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RoomCameraHeader(
    modifier: Modifier = Modifier,
    roomCode: String?,
    cameraName: String?
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        roomCode?.let {
            Text(
                text = it,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
        cameraName?.let {
            Text(
                text = "Camera: $it",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}
