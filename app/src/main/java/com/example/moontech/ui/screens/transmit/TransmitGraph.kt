package com.example.moontech.ui.screens.transmit

import android.Manifest
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.moontech.data.dataclasses.AppError
import com.example.moontech.ui.components.PermissionWrapper
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.transmitGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Transmit.Camera.route
    navigation(startDestination = startDestination, route = Screen.Transmit.route) {
        composable(
            Screen.Transmit.Camera.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) {
            PermissionWrapper(permission = Manifest.permission.CAMERA, modifier = modifier) {
                val isStreaming by viewModel.isStreamingState.collectAsState()
                val parentEntry = remember(it) { navController.getBackStackEntry(Screen.Transmit.route) }
                val roomCode = parentEntry.arguments?.getString("code")!!
                val roomCameras by  viewModel.roomCameras.collectAsState()
                val roomCamera = roomCameras.find { it.code == roomCode }
                if (roomCamera == null) {
                    LaunchedEffect(key1 = true) {
                        viewModel.emitError(AppError.Error("Camera not found"))
                    }
                } else {
                    TransmittingScreen(
                        roomCamera = roomCamera,
                        modifier = modifier,
                        isStreaming = isStreaming,
                        startStream = viewModel::startStream,
                        startPreview = viewModel::startPreview,
                        stopStream = viewModel::stopStream,
                        stopPreview = viewModel::stopPreview
                    )
                }
            }
        }
    }
}