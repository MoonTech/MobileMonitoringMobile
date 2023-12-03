package com.example.moontech.ui.screens.transmit

import android.Manifest
import android.util.Log
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
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.PermissionWrapper
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.navigation.navigateToScreenWithCode
import com.example.moontech.ui.screens.common.RoomType
import com.example.moontech.ui.viewmodel.AppViewModel

private const val TAG = "TransmitGraph"

fun NavGraphBuilder.transmitGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Transmit.Camera.route
    navigation(startDestination = startDestination, route = Screen.Transmit.route) {

        composable(
            Screen.Transmit.Camera.route,
            arguments = listOf(navArgument("code") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            PermissionWrapper(permission = Manifest.permission.CAMERA, modifier = modifier) {
                val isStreaming by viewModel.isStreamingState.collectAsState()
                val transmittingRoomCode by viewModel.transmittingRoomCode.collectAsState()
                val roomCode = if (isStreaming) {
                    transmittingRoomCode
                } else {
                    it.arguments?.getString("code")
                }
                val roomCameras by viewModel.roomCameras.collectAsState()
                val roomCamera = roomCameras.find { it.code == roomCode }
                if (roomCode != null && roomCamera == null) {
                    LaunchedEffect(true) {
                        viewModel.emitError(AppError.Error("Camera attached to a Room '$roomCode' doesn't exist"))
                    }
                } else {
                    TransmittingScreen(
                        roomCamera = roomCamera,
                        modifier = modifier,
                        isStreaming = isStreaming,
                        startStream = viewModel::startStream,
                        startPreview = viewModel::startPreview,
                        stopStream = viewModel::stopStream,
                        stopPreview = viewModel::stopPreview,
                        selectCamera = {
                            navController.navigate(Screen.Transmit.SelectCamera.route)
                        }
                    )
                }
            }
        }

        composable(Screen.Transmit.AddRoom.route) {
            val parentEntry =
                remember(it) { navController.getBackStackEntry(Screen.Transmit.route) }
            val roomCode = parentEntry.arguments?.getString("code")!!
            TransmitAddRoomScreen(modifier = modifier,
                addRoomCamera = { _, password ->
                    viewModel.addRoomCamera(roomCode, password) {
                        navController.popBackStack()
                    }
                })
        }

        composable(Screen.Transmit.SelectCamera.route) {
            val externalRooms by viewModel.externalRoomCameras.collectAsState()
            val rooms = mutableMapOf<RoomType, List<ObjectWithRoomCode>>(
                Pair(RoomType.EXTERNAL, externalRooms)
            )
            val myRooms by viewModel.myRooms.collectAsState()
            if (myRooms.isNotEmpty()) rooms[RoomType.MY_ROOMS] = myRooms
            SelectRoomCameraScreen(
                modifier = modifier,
                rooms = rooms,
                addCamera = { /*TODO*/ },
                selectCamera = {
                    Log.i(TAG, "transmitGraph: Camera selected ${it.code}")
                    navController.navigateToScreenWithCode(Screen.Transmit.Camera, it.code) {
                        popUpTo(Screen.Transmit.Camera.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}