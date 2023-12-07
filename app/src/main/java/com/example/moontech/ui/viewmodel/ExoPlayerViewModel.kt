package com.example.moontech.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.moontech.ui.screens.watch.buildExoPlayer

class ExoPlayerViewModel(application: Application): AndroidViewModel(application) {
    private val player = mutableStateOf(buildExoPlayer(getApplication()))
    val playerState: State<ExoPlayer> = player
    companion object {
        private const val TAG = "ExoPlayerViewModel"
    }

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
        }
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared: clearing")
        player.value.release()
        super.onCleared()
    }
}