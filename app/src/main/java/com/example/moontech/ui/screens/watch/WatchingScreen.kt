package com.example.moontech.ui.screens.watch

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.example.moontech.data.dataclasses.WatchedRoom
import com.example.moontech.data.dataclasses.WatchedRoomCamera
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.MenuDrawer
import com.example.moontech.ui.components.RememberScreenOrientation

private const val TAG = "WatchingScreen"

@kotlin.OptIn(ExperimentalAnimationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun WatchingScreen(
    modifier: Modifier = Modifier,
    watchedRoom: WatchedRoom,
    init: (playerView: PlayerView) -> Unit,
    play: (mediaItem: MediaItem) -> Unit,
    stop: () -> Unit,
    enterFullScreen: () -> Unit,
    exitFullScreen: () -> Unit
) = CenterScreen(modifier) {
    // TODO: Make controls custom
    val cameras = remember {
        mutableStateListOf(
            WatchedRoomCamera("Cam 1", "Cam 1", true),
            WatchedRoomCamera("Cam 2", "Cam 2", true),
            WatchedRoomCamera("Cam 3", "Cam 3", true),
            WatchedRoomCamera("Cam 4", "Cam 4", true),
        )
    }
    DisposableEffect(true) {
        onDispose {
            Log.i(TAG, "WatchingScreen: stopping")
            stop()
        }
    }
    var showControls by remember { mutableStateOf(false) }
    var fullScreen by rememberSaveable { mutableStateOf(false) }
    val orientation = if (fullScreen) {
        SCREEN_ORIENTATION_LANDSCAPE
    } else {
        -1
    }

    val orientationState = RememberScreenOrientation(orientation,
        onPortrait = { exitFullScreen() },
        onLandscape = { enterFullScreen() },
        onDispose = { exitFullScreen() })
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                PlayerView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
                        showControls = when (visibility) {
                            View.VISIBLE -> true
                            else -> false
                        }
                    })
                    init(this)
                    val mediaItem =
                        buildHlsMediaItem("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
                    play(mediaItem)
                }
            })

            androidx.compose.animation.AnimatedVisibility(visible = showControls) {
                MenuDrawer(modifier = Modifier.animateEnterExit(enter = fadeIn(), exit = fadeOut()),
                    items = cameras,
                    header = {
                        Text(
                            text = "Select camera",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                        )
                        Divider()
                    },
                    itemContent = {
                        Text(text = it.id,
                            modifier = Modifier
                                .clickable { }
                                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                                .fillMaxWidth())
                    })
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = showControls,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 50.dp, bottom = 9.dp)
            ) {
                IconButton(
                    onClick = { fullScreen = !fullScreen },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.BottomEnd)
                        .animateEnterExit(enter = fadeIn(), exit = fadeOut())
                ) {
                    if (fullScreen) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp),
                            imageVector = Icons.Filled.FullscreenExit,
                            contentDescription = "full screen",
                            tint = Color.White,
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(32.dp),
                            imageVector = Icons.Filled.Fullscreen,
                            contentDescription = "full screen",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}