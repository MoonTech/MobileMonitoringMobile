@file:UnstableApi

package com.example.moontech.ui.screens.watch

import android.Manifest
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.PermissionWrapper
import com.example.moontech.ui.components.hideSystemUi
import com.example.moontech.ui.components.showSystemUi
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.navigation.navigateToScreenWithCode
import com.example.moontech.ui.viewmodel.AppViewModel
import com.example.moontech.ui.viewmodel.ExoPlayerViewModel
import com.example.moontech.ui.viewmodel.ExoPlayerViewModelFactoryProvider

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
            val exoPlayerViewModel: ExoPlayerViewModel =
                viewModel(factory = ExoPlayerViewModelFactoryProvider.Factory)
            val parentEntry = remember(it) { navController.getBackStackEntry(Screen.Watch.route) }
            val roomCode = parentEntry.arguments?.getString("code")
            if (roomCode == null) {
                viewModel.emitError(AppState.Error("Code is null"))
                return@composable
            }

            val watchedRoom by viewModel.watchedRoom.collectAsState()
            Log.i(TAG, "watchGraph: $watchedRoom")
            LaunchedEffect(true) {
                viewModel.watch(roomCode)
            }
            if (watchedRoom?.code != roomCode) {
                CenterScreen(modifier = modifier) {
                    CircularProgressIndicator()
                }
            } else {
                val context = LocalContext.current
                val recordingCameras by viewModel.isRecording.collectAsState()
                PermissionWrapper(
                    modifier = modifier,
                    permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    apply = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU
                ) {
                    WatchingScreen(
                        modifier = modifier,
                        watchedRoom = watchedRoom!!,
                        init = { view -> exoPlayerViewModel.init(view) },
                        play = { mediaItem -> exoPlayerViewModel.play(mediaItem) },
                        stop = { exoPlayerViewModel.stop() },
                        enterFullScreen = {
                            viewModel.hideNavigation()
                            context.hideSystemUi()
                        },
                        exitFullScreen = {
                            viewModel.showNavigation()
                            context.showSystemUi()
                        },
                        startRecording = { camera -> viewModel.startRecording(camera) },
                        stopRecording = { camera -> viewModel.stopRecording(roomCode, camera) },
                        recordingCameras = recordingCameras,
                        navigateToRecordings = {
                            viewModel.fetchRecordings(watchedRoom!!.code)
                            viewModel.hideNavigation()
                            navController.navigateToScreenWithCode(
                                Screen.Watch.Recordings,
                                watchedRoom!!.code
                            )
                        },
                        exit = { navController.popBackStack() }
                    )
                }
            }
        }
        composable(Screen.Watch.Recordings.route, arguments = listOf(navArgument("code") {
            type = NavType.StringType
        })) {
            val code = it.arguments?.getString("code")
            val recordings by viewModel.recordings.collectAsState()
            BackHandler {
                navController.popBackStack()
                viewModel.showNavigation()
                Log.i(TAG, "watchGraph: navigation shown")
            }
            if (recordings == null || recordings!!.code != code) {
                CenterScreen(modifier = modifier) {
                    CircularProgressIndicator()
                }
            } else {
                RecordingsScreen(
                    recordings = recordings!!,
                    modifier = modifier,
                    onRecordingClicked = {
                        viewModel.downloadRecording(it)
                    }
                )
            }
        }

    }
}