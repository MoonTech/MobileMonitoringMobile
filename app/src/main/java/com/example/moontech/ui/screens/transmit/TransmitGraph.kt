package com.example.moontech.ui.screens.transmit

import android.Manifest
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.moontech.ui.components.PermissionWrapper
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.transmitGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Transmit.Main.route
    navigation(startDestination = startDestination, route = Screen.Transmit.route) {
        composable(Screen.Transmit.Main.route) {
            TransmitMainScreen(viewModel = viewModel, modifier = modifier, addRoom = {
                navController.navigate(Screen.Transmit.AddRoom.route)
            }, onClick = { room ->
                navController.navigate(Screen.Transmit.Camera.route.replace("{code}", room.code))
            })
        }
        composable(Screen.Transmit.AddRoom.route) {
            TransmitAddRoomScreen(addRoomCamera = { code, password ->
                viewModel.addRoomCamera(
                    code,
                    password
                )
                navController.popBackStack(Screen.Transmit.Main.route, inclusive = false)
            }, modifier = modifier)
        }
        composable(
            Screen.Transmit.Camera.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) {
            PermissionWrapper(permission = Manifest.permission.CAMERA, modifier = modifier) {
                val isStreaming by viewModel.isStreamingState.collectAsState()
                val roomCode = it.arguments?.getString("code")!!
                val roomCameras by  viewModel.roomCameras.collectAsState()
                val roomCamera = roomCameras.find { it.code == roomCode }!!
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