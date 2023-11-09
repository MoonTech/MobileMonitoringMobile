package com.example.moontech.ui.screens.watch

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.screens.roomlogin.RoomLoginScreen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.watchGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Watch.Main.route
    navigation(startDestination = startDestination, route = Screen.Watch.route) {
        composable(Screen.Watch.Main.route) {
            RoomLoginScreen(
                viewModel = viewModel,
                onRoomLoggedIn = {
                    navController.navigate(Screen.Watch.Watching.route) {
                        popUpTo(route = startDestination)
                    }
                },
                requiredPrivilege = { this.canWatch() },
                onConfirm = { code, password -> viewModel.loginToRoomForWatching(code, password) },
                modifier = modifier
            )
        }
        composable(Screen.Watch.AddRoom.route) {

        }
        composable(Screen.Watch.Watching.route) {
            WatchingScreen(modifier = modifier, viewModel = viewModel)
        }
    }
}