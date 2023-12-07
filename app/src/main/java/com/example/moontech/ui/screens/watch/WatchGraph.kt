@file:UnstableApi package com.example.moontech.ui.screens.watch

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
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
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.viewmodel.AppViewModel

private const val TAG = "WatchGraph"
fun NavGraphBuilder.watchGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.Watch.Watching.route
    navigation(startDestination = startDestination, route = Screen.Watch.route) {
        composable(
            Screen.Watch.Watching.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) {
            val parentEntry = remember(it) { navController.getBackStackEntry(Screen.Watch.route) }
            val roomCode = parentEntry.arguments?.getString("code")
            if (roomCode == null) {
                viewModel.emitError(AppState.Error("Code is null"))
                return@composable
            }

            val watchedRoom by viewModel.watchedRoom.collectAsState()
            Log.i(TAG, "watchGraph: $watchedRoom")
            if (watchedRoom?.code != roomCode) {
                LaunchedEffect(true) {
                    viewModel.watch(roomCode)
                }
                CenterScreen(modifier = modifier) {
                    CircularProgressIndicator()
                }
            } else {
                WatchingScreen(modifier = modifier, watchedRoom = watchedRoom!!)
            }
        }
    }
}