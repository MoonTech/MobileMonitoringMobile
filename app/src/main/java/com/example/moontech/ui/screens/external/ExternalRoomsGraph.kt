package com.example.moontech.ui.screens.external

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
                    var showMenu by remember { mutableStateOf(false) }
                    Column {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Setting",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .clickable {
                                    showMenu = true
                                }
                                .fillMaxHeight()
                        )
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            Text(text = "Delete", modifier = Modifier
                                .clickable {
                                    viewModel.removeExternalRoom(it.code)
                                }
                                .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
                            )
                        }
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