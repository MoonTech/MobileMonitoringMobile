package com.example.moontech.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun FloatingActionButtonWithIcon(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    if (visible) {
        FloatingActionButton(onClick = onClick, modifier = modifier) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    }
}