package com.example.moontech.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LogOutDialog(dismissDialog: () -> Unit, logOut: () -> Unit) {
    AlertDialog(
        onDismissRequest = dismissDialog,
        text = { Text(text = "Are you sure that you want to log out?") },
        confirmButton = {
            TextButton(onClick = logOut) {
                Text(text = "Log out")
            }
        },
        dismissButton = {
            TextButton(onClick = dismissDialog) {
                Text(text = "Cancel")
            }
        }
    )
}