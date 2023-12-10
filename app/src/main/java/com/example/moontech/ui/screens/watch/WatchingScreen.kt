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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.example.moontech.data.dataclasses.WatchedRoom
import com.example.moontech.data.dataclasses.WatchedRoomCamera
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.WatchingScreenInfoPanel

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
    exitFullScreen: () -> Unit,
    startRecording: (WatchedRoomCamera) -> Unit,
    stopRecording: (WatchedRoomCamera) -> Unit,
    isRecording: Boolean
) = CenterScreen(modifier) {
    // TODO: Make controls custom
    // TODO: Recording is now global, make it per room-camera
    val context = LocalContext.current
    DisposableEffect(true) {
        onDispose {
            Log.i(TAG, "WatchingScreen: stopping")
            stop()
            exitFullScreen()
        }
    }
    var selectedCameraName by rememberSaveable {
        mutableStateOf(watchedRoom.connectedCameras.firstOrNull()?.cameraName ?: "")
    }
    val selectedCamera =
        watchedRoom.connectedCameras.firstOrNull() { it.cameraName == selectedCameraName }
    LaunchedEffect(selectedCamera) {
        selectedCamera?.let {
            val mediaItem = buildHlsMediaItem(it.watchUrl)
            play(mediaItem)
        }
    }

    var showControls by remember { mutableStateOf(false) }
    var fullScreen by rememberSaveable { mutableStateOf(false) }
    val orientation = if (fullScreen) {
        SCREEN_ORIENTATION_LANDSCAPE
    } else {
        -1
    }
//    val orientationState = RememberScreenOrientation(orientation,
//        onPortrait = { exitFullScreen() },
//        onLandscape = { enterFullScreen() },
//        onDispose = { exitFullScreen() })

    Column(modifier = Modifier.fillMaxSize()) {
        if (!fullScreen) {
            WatchingScreenInfoPanel(
                watchedRoom = watchedRoom,
                selectedCameraName = selectedCameraName,
                onCameraClicked = { selectedCameraName = it.cameraName }
            )
        }
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
                    selectedCamera?.let {
                        val mediaItem = buildHlsMediaItem(it.watchUrl)
                        play(mediaItem)
                    }
                }
            })
            androidx.compose.animation.AnimatedVisibility(
                visible = showControls,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 100.dp, bottom = 7.dp)
            ) {
                TextButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .animateEnterExit(enter = fadeIn(), exit = fadeOut()),
                    onClick = {
                        if (isRecording) {
                            selectedCamera?.let {
                                stopRecording(it)
                            }
                        } else {
                            selectedCamera?.let {
                                startRecording(it)
                            }
                        }
                    }) {
                    Text(
                        text = if (isRecording) "Stop recording" else "Start recording",
                        color = Color.White
                    )
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = showControls,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 50.dp, bottom = 9.dp)
            ) {
                IconButton(
                    onClick = {
                        fullScreen = !fullScreen
                        if (fullScreen) {
                            enterFullScreen()
                        } else {
                            exitFullScreen()
                        }
                    },
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