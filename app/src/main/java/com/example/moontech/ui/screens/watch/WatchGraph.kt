package com.example.moontech.ui.screens.watch

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.watchGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Watch.Main.route
    navigation(startDestination = startDestination, route = Screen.Watch.route) {
        composable(Screen.Watch.Main.route) {
            WatchMainScreen(
                viewModel = viewModel,
                modifier = modifier,
                addRoom = { navController.navigate(Screen.Watch.AddRoom.route) },
                onClick = { })
        }
        composable(Screen.Watch.AddRoom.route) {
            WatchAddRoomScreen(modifier = modifier,
                onAddRoom = { code, password ->
                    viewModel.addExternalRoom(code, password)
                    navController.popBackStack(Screen.Watch.Main.route, inclusive = false)
                })
        }
        composable(Screen.Watch.Watching.route) {
            WatchingScreen(modifier = modifier, viewModel = viewModel)
        }
    }
}