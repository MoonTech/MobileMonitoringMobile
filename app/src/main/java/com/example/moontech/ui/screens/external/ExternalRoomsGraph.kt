package com.example.moontech.ui.screens.external

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.components.DeleteDrawer
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.navigation.navigateToScreenWithCode
import com.example.moontech.ui.screens.base.MainScreenBase
import com.example.moontech.ui.screens.transmit.ScanQRCodeScreen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.externalRoomsGraph(
    navController: NavController, viewModel: AppViewModel, modifier: Modifier
) {
    val startDestination = Screen.ExternalRooms.Main.route
    navigation(startDestination = startDestination, route = Screen.ExternalRooms.route) {

        composable(Screen.ExternalRooms.Main.route) {
            val rooms by viewModel.externalRooms.collectAsState()

            MainScreenBase(rooms = rooms,
                modifier = modifier,
                addRoom = {
                    navController.navigate(Screen.ExternalRooms.AddRoom.route)
                },
                onSettings = {},
                onClick = {
                    navController.navigateToScreenWithCode(Screen.Watch, it.code)
                },
                topBar = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = stringResource(Screen.ExternalRooms.label),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                },
                rowTileContent = {
                    DeleteDrawer {
                       viewModel.removeExternalRoom(it.code)
                    }
                })
        }

        composable(Screen.ExternalRooms.AddRoom.route) {
            ExternalRoomsAddRoomScreen(
                addRoom = { code, password ->
                    viewModel.addExternalRoom(code, password)
                    navController.popBackStack(Screen.ExternalRooms.Main.route, inclusive = false)
                },
                emitError = {
                    viewModel.emitError(it)
                },
                modifier = modifier,
                onAddWithQrCode = {
                    navController.navigate(Screen.ExternalRooms.AddRoomQrCode.route)
                }
            )
        }

        composable(Screen.ExternalRooms.AddRoomQrCode.route) {
            val qrCodeContent by viewModel.lastQrCode.collectAsState()
            ScanQRCodeScreen(
                modifier = modifier,
                startPreview = {
                    viewModel.startPreview(it)
                    viewModel.startQrCodeScanner()
                },
                stopPreview = { viewModel.stopPreview() },
                startScanning = { viewModel.startQrCodeScanner() },
                stopScanning = { viewModel.stopQrCodeScanner() },
                addRoom = {
                    viewModel.addExternalRoom(it)
                    navController.popBackStack(Screen.ExternalRooms.Main.route, false)
                },
                qrCodeContent = qrCodeContent
            )
        }
    }
}