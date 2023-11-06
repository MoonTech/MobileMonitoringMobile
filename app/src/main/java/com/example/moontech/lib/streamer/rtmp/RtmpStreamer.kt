package com.example.moontech.lib.streamer.rtmp

import android.content.Context
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.example.moontech.lib.streamer.Streamer

class RtmpStreamer(private val context: Context) : Streamer {
    companion object {
        private const val TAG = "RtmpStreamer"
    }

    private val pipes = mutableMapOf<String, StreamSession>()

    override fun startStream(url: String, streamCommand: StreamCommand): String {
        val pipe = FFmpegKitConfig.registerNewFFmpegPipe(context)
        val command = streamCommand.withOutputInfoToString(pipe, url)
        val session = FFmpegKit.executeAsync(command, { session ->
            Log.i(TAG, "startStream: session completed $session")
        }, { log ->
            Log.i(TAG, "stream log: ${log.message}")
        }, { statistics ->
            Log.i(TAG, "stream statistics: $statistics")
        })
        pipes[url] = StreamSession(pipe, session)
        return pipe
    }

    override fun endStream(url: String) {
        Log.i(TAG, "endStream: stream end request")
        val sessionToDelete = pipes[url]
        if (sessionToDelete != null) {
            FFmpegKitConfig.closeFFmpegPipe(sessionToDelete.pipe)
            sessionToDelete.session.cancel()
            Log.i(TAG, "endStream: stream ended")
        } else {
            Log.i(TAG, "endStream: no session for given url")
        }
    }

    private fun StreamCommand.withOutputInfoToString(inpurUrl: String, outputUrl: String): String {
        return "${this.copy(inputUrl = inpurUrl)} -f flv -r 30 $outputUrl"
    }

}