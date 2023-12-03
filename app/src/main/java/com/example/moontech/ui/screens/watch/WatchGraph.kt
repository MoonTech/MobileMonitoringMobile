@file:UnstableApi package com.example.moontech.ui.screens.watch

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.viewmodel.AppViewModel

private const val TAG = "WatchGraph"
fun NavGraphBuilder.watchGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Watch.Main.route
    navigation(startDestination = startDestination, route = Screen.Watch.route) {
        composable(Screen.Watch.Main.route) {

        }
        composable(
            Screen.Watch.Watching.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Screen.Transmit.route)
            }
            val roomCode = parentEntry.arguments?.getString("code")!!
            val watchedRoom by viewModel.watchedRoom.collectAsState()
            WatchingScreen(modifier = modifier, watchedRoom = watchedRoom!!)
        }
    }
}