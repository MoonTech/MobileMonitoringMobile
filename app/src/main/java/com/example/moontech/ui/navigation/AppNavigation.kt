@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.moontech.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moontech.ui.components.PermissionWrapper
import com.example.moontech.ui.screens.home.HomeScreen
import com.example.moontech.ui.screens.roomlogin.RoomLoginScreen
import com.example.moontech.ui.screens.transmit.TransmittingScreen
import com.example.moontech.ui.screens.watching.WatchingScreen
import com.example.moontech.ui.viewmodel.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
fun AppNavigation(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            HomeScreen(
                onCreateRoom = { },
                onAddCamera = { navController.navigate(Screen.RoomTransmissionLogin.route) },
                onWatchTransmission = { navController.navigate(Screen.RoomLogin.route) },
                modifier = modifier
            )
        }
        composable(route = Screen.RoomLogin.route) {
            RoomLoginScreen(
                viewModel = viewModel,
                onRoomLoggedIn = {
                    navController.navigate(Screen.Watching.route) {
                        popUpTo(route = Screen.Main.route)
                    }
                },
                requiredPrivilege = { this.canWatch() },
                onConfirm = {code, password -> viewModel.loginToRoomForWatching(code, password)},

                modifier = modifier
            )
        }
        composable(route = Screen.RoomTransmissionLogin.route) {
            RoomLoginScreen(
                viewModel = viewModel,
                onRoomLoggedIn = {
                    navController.navigate(Screen.Transmitting.route) {
                        popUpTo(route = Screen.Main.route)
                    }
                },
                requiredPrivilege = { this.canTransmit() },
                onConfirm = {code, password -> viewModel.loginToRoomForTransmitting(code, password)},
                modifier = modifier
            )
        }
        composable(route = Screen.Watching.route) {
            WatchingScreen(modifier = modifier, viewModel = viewModel)
        }
        composable(route = Screen.Transmitting.route) {
            PermissionWrapper(permission = android.Manifest.permission.CAMERA, modifier = modifier) {
                val uiState by viewModel.uiState.collectAsState()
                val isPreview by viewModel.isPreviewState.collectAsState()
                val isStreaming by viewModel.isStreamingState.collectAsState()
                TransmittingScreen(
                    modifier = modifier,
                    uiState = uiState,
                    isStreaming = isStreaming,
                    isPreview = isPreview,
                    startStream = viewModel::startStream,
                    startPreview = viewModel::startPreview,
                    stopStream = viewModel::stopStream,
                    stopPreview = viewModel::stopPreview)
            }
        }
    }

}