package com.example.moontech.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.moontech.ui.screens.watch.buildExoPlayer

class ExoPlayerViewModel(application: Application): AndroidViewModel(application) {
    val player: State<ExoPlayer> = mutableStateOf(buildExoPlayer(getApplication()))


    fun init(playerView: PlayerView) {
        playerView.player = player.value
    }

    fun play(mediaItem: MediaItem) {
        player.value.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    fun stop() {
        player.value.apply {
            stop()
            clearMediaItems()
        }
    }

    override fun onCleared() {
        player.value.release()
        super.onCleared()
    }
}