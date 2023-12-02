package com.example.moontech.ui.screens.external

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.navigation.navigateToScreenWithCode
import com.example.moontech.ui.screens.base.MainScreenBase
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
                onTransmit = { navController.navigateToScreenWithCode(Screen.Transmit, it.code) },
                onWatch = { navController.navigate(Screen.Watch.route) })
        }

        composable(Screen.ExternalRooms.AddRoom.route) {
            ExternalRoomsAddRoomScreen(addRoom = { code, password ->
                viewModel.addExternalRoom(code, password)
                navController.popBackStack(Screen.ExternalRooms.Main.route, inclusive = false)
            }, modifier = modifier)
        }

    }
}