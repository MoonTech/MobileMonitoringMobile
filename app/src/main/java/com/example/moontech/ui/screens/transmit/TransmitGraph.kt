package com.example.moontech.ui.screens.transmit

import android.Manifest
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.components.PermissionWrapper
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.screens.roomlogin.RoomLoginScreen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.transmitGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Transmit.Main.route
    navigation(startDestination = startDestination, route = Screen.Transmit.route) {
        composable(Screen.Transmit.Main.route) {
            RoomLoginScreen(
                viewModel = viewModel,
                onRoomLoggedIn = {
                    navController.navigate(Screen.Transmit.Camera.route) {
                        popUpTo(route = Screen.Transmit.route)
                    }
                },
                requiredPrivilege = { true },
                onConfirm = {code, password -> },
                modifier = modifier
            )
        }
        composable(Screen.Transmit.AddRoom.route) {

        }
        composable(Screen.Transmit.Camera.route) {
            PermissionWrapper(permission = Manifest.permission.CAMERA, modifier = modifier) {
                val uiState by viewModel.uiState.collectAsState()
                val isStreaming by viewModel.isStreamingState.collectAsState()
                TransmittingScreen(
                    modifier = modifier,
                    uiState = uiState,
                    isStreaming = isStreaming,
                    startStream = viewModel::startStream,
                    startPreview = viewModel::startPreview,
                    stopStream = viewModel::stopStream,
                    stopPreview = viewModel::stopPreview)
            }
        }
    }
}