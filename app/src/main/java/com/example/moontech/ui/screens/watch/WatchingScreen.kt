package com.example.moontech.ui.screens.watch

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.example.moontech.data.dataclasses.WatchedRoom
import com.example.moontech.data.dataclasses.WatchedRoomCamera
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.MenuDrawer

@Composable
fun WatchingScreen(
    modifier: Modifier = Modifier,
    watchedRoom: WatchedRoom
) = CenterScreen(modifier) {
    val context = LocalContext.current
    // TODO: Can't use it like that, player restarts every time device is rotated
    val player by remember {
        mutableStateOf(buildExoPlayer(context))
    }
    val cameras = remember {
        mutableStateListOf(
            WatchedRoomCamera("Cam 1", "Cam 1",  true),
            WatchedRoomCamera("Cam 2", "Cam 2",  true),
            WatchedRoomCamera("Cam 3", "Cam 3",  true),
            WatchedRoomCamera("Cam 4", "Cam 4",  true),
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            MenuDrawer(
                items = cameras,
                header = {
                Text(
                    text = "Select camera",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp))
                Divider()
                },
                itemContent = {
                    Text(
                        text = it.id,
                        modifier = Modifier
                            .clickable { }
                            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                            .fillMaxWidth())
                })
        }
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                PlayerView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.player = player
                        .apply {
                            val mediaItem =
                                buildHlsMediaItem("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
                            setMediaItem(mediaItem)
                            prepare()
                            play()
                        }
                }
            }
        )
    }
}