package com.example.moontech.ui.screens.watch

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.example.moontech.data.dataclasses.WatchedRoom
import com.example.moontech.ui.components.CenterScreen

@Composable
fun WatchingScreen(
    modifier: Modifier = Modifier,
    watchedRoom: WatchedRoom
) = CenterScreen(modifier) {
    val context = LocalContext.current
    val player by remember {
        mutableStateOf(buildExoPlayer(context))
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
                        val mediaItem = buildHlsMediaItem("")
                        setMediaItem(mediaItem)
                        prepare()
                        play()
                    }
            }
        }
    )
}