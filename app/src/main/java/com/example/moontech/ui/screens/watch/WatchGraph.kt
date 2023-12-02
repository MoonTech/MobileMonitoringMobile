@file:UnstableApi package com.example.moontech.ui.screens.watch

import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.moontech.ui.components.CenterScreen
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
            CenterScreen {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        PlayerView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            player = ExoPlayer.Builder(context).setMediaSourceFactory(
                                DefaultMediaSourceFactory (context).setLiveTargetOffsetMs(5000)
                            ).build().apply {
                                val mediaItem = MediaItem.Builder()
                                    .setUri("http://192.168.0.109:8080/hls/test5.m3u8")
                                    .setLiveConfiguration(
                                        MediaItem.LiveConfiguration.Builder()
                                            .setMaxPlaybackSpeed(1.02f)
                                            .build())
                                    .build();
                                setMediaItem(mediaItem)
                                addAnalyticsListener(EventLogger())
                                addListener(object : Player.Listener {
                                    override fun onPlayerError(error: PlaybackException) {
                                        Log.e(TAG, "onPlayerError: ", error)
                                        seekToDefaultPosition()
                                        prepare()
                                    }
                                })
                                prepare()
                                play()
                            }
                        }
                    }
                )
            }
        }
        composable(
            Screen.Watch.Watching.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Screen.Transmit.route)
            }
            val roomCode = parentEntry.arguments?.getString("code")!!
            WatchingScreen(modifier = modifier, viewModel = viewModel)
        }
    }
}