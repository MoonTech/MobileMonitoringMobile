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

    override fun startStream(url: String): String {
        val pipe = FFmpegKitConfig.registerNewFFmpegPipe(context)
        val command = generateStreamCommand(pipe, url)
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

    private fun generateStreamCommand(inpurUtl: String, outputUrl: String): String {
        return FFmpegStreamCommand(
            inputFormat = "rawvideo",
            inputPixelFormat = "nv21",
            inputVideoSize = "640x480",
            inputFrameRate = 20,
            inputUrl = inpurUtl,
            encoder = "libx264",
            encoderSettings = "-profile baseline -preset veryfast -pix_fmt nv21",
            outputFormat = "flv",
            outputUrl = outputUrl
        ).toString()
    }

}