@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.moontech.ui.components

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun PermissionWrapper(
    permission: String,
    modifier: Modifier = Modifier,
    apply: Boolean = true,
    content: @Composable () -> Unit
) {
    if (apply) {
        val cameraPermissionState = rememberPermissionState(
            permission
        )
        Log.e("t", "${cameraPermissionState.status.isGranted}")

        when {
            cameraPermissionState.status.isGranted -> content()
            else -> CenterScreen(modifier) {
                LaunchedEffect(key1 = true) {
                    cameraPermissionState.launchPermissionRequest()
                }
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text(text = "Request permission")
                }
            }

        }
    } else {
        content()
    }

}