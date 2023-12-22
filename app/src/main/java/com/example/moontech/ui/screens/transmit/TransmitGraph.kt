package com.example.moontech.ui.screens.transmit

import android.Manifest
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.moontech.R
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.data.dataclasses.QrCodeContent
import com.example.moontech.ui.components.PermissionWrapper
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.navigation.navigateToScreenWithCode
import com.example.moontech.ui.navigation.navigateWithParams
import com.example.moontech.ui.screens.base.AuthScreenBase
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
                        viewModel.emitError(AppState.Error("Camera attached to a Room '$roomCode' doesn't exist"))
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

        composable(Screen.Transmit.SelectCamera.route) {
            val externalRooms by viewModel.externalRoomCameras.collectAsState()
            val myRoomCameras by viewModel.myRoomCameras.collectAsState()
            val rooms = mutableMapOf<RoomType, List<ObjectWithRoomCode>>(
                Pair(RoomType.EXTERNAL, externalRooms)
            )
            val myRooms by viewModel.myRooms.collectAsState()
            if (myRooms.isNotEmpty()) rooms[RoomType.MY_ROOMS] = myRooms
            SelectRoomCameraScreen(
                modifier = modifier,
                rooms = rooms,
                addRoom = {
                    navController.navigate(Screen.Transmit.AddRoom.route)
                },
                selectCamera = { room ->
                    Log.i(TAG, "transmitGraph: Camera selected ${room.code}")
                    if (myRooms.any { it.code == room.code } && myRoomCameras.none { it.code == room.code }) {
                        navController.navigateWithParams(
                            Screen.Transmit.AddCamera,
                            mapOf(Pair("code", room.code), Pair("password", null))
                        )
                    } else {
                        navController.navigateToScreenWithCode(Screen.Transmit.Camera, room.code) {
                            popUpTo(Screen.Transmit.Camera.route) {
                                inclusive = true
                            }
                        }
                    }
                },
            )
        }

        composable(Screen.Transmit.AddRoom.route) {
            AuthScreenBase(
                modifier = modifier,
                firstButtonLabel = R.string.next,
                firstTextFieldLabel = R.string.room_name,
                secondTextFieldLabel = R.string.password,
                screenLabel = R.string.add_camera,
                secondButtonLabel = R.string.add_qr_code,
                emitError = {
                    viewModel.emitError(it)
                },
                firstButtonAction = { code, password ->
                    navController.navigateWithParams(
                        Screen.Transmit.AddCamera,
                        mapOf(Pair("code", code), Pair("password", password))
                    )
                },
                secondButtonAction = {
                    navController.navigate(Screen.Transmit.AddRoomQrCode.route)
                }
            )
        }

        composable(Screen.Transmit.AddRoomQrCode.route) {
            ScanQRCodeScreen(
                modifier = modifier,
                startPreview = {
                    viewModel.startPreview(it)
                    viewModel.startQrCodeScanner()
                },
                stopPreview = { viewModel.stopPreview() },
                startScanning = { viewModel.startQrCodeScanner() },
                stopScanning = { viewModel.stopQrCodeScanner() },
                addCameraToRoom = { },
                qrCodeContent = QrCodeContent("Room 1")
            )
        }

        composable(Screen.Transmit.AddCamera.route, arguments = listOf(
            navArgument("code") { type = NavType.StringType },
            navArgument("password") {
                type = NavType.StringType
                nullable = true
            }
        )) { navBackStackEntry ->
            val roomCode = navBackStackEntry.arguments?.getString("code")
            val password = navBackStackEntry.arguments?.getString("password")
            if (roomCode == null) {
                LaunchedEffect(true) {
                    viewModel.emitError(AppState.Error("Something went wrong"))
                }
                return@composable
            }
            val appState by viewModel.authState.collectAsState()
            AuthScreenBase(
                modifier = modifier,
                appState = appState,
                firstButtonLabel = R.string.add,
                firstTextFieldLabel = R.string.camera_name,
                screenLabel = R.string.add_camera,
                emitError = { viewModel.emitError(it) },
                firstButtonAction = { cameraName, _ ->
                    viewModel.addRoomCamera(
                        cameraName = cameraName,
                        roomCode = roomCode,
                        password = password,
                        roomType = if (password == null) RoomType.MY_ROOMS else RoomType.EXTERNAL,
                        onSuccess = {
                            navController.navigateToScreenWithCode(
                                Screen.Transmit.Camera,
                                roomCode
                            ) {
                                popUpTo(Screen.Transmit.Camera.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            )
        }
    }
}