package com.example.moontech.lib.streamer.rtmp

import android.content.Context
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.example.moontech.lib.streamer.Streamer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RtmpStreamer(private val context: Context) : Streamer {
    companion object {
        private const val TAG = "RtmpStreamer"
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    private val pipes = mutableMapOf<String, StreamSession>()
    private var currentTimeJob: Job? = null

    override fun startStream(
        url: String,
        streamCommand: StreamCommand,
        onStreamFailedCallback: () -> Unit
    ): String {
        restartCurrentTimeJob()
        val file = File(context.filesDir, "text.txt")
        FFmpegKitConfig.setFontDirectory(context, "/system/fonts", mapOf())
        val pipe = FFmpegKitConfig.registerNewFFmpegPipe(context)
        val command = streamCommand.withOutputInfoToString(pipe, url, file.absolutePath)
        val session = FFmpegKit.executeAsync(command, { session ->
            Log.i(TAG, "startStream: session completed $session")
            if (session.returnCode.isValueError) {
                currentTimeJob?.cancel()
                onStreamFailedCallback()
            }
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
            currentTimeJob?.cancel()
            Log.i(TAG, "endStream: stream ended")
        } else {
            Log.i(TAG, "endStream: no session for given url")
        }
    }

    override fun endAllStreams() {
        pipes.keys.forEach(this::endStream)
    }

    private fun StreamCommand.withOutputInfoToString(inpurUrl: String, outputUrl: String, textFile: String): String {
        val additionalFilter = "drawtext=textfile=$textFile:reload=1:fontcolor=white:fontsize=24:box=1:boxcolor=black@0.5:boxborderw=10:x=10:y=1 [video_out]; anullsrc=cl=mono:r=44100[audio_out]\" -map [video_out] -map [audio_out] "
        return "${this.copy(inputUrl = inpurUrl, filters = filters  + additionalFilter)} -f flv $outputUrl"
    }

    private fun restartCurrentTimeJob() {
        currentTimeJob?.cancel()
        val file = File(context.filesDir, "text.txt")
        file.createNewFile()
        currentTimeJob = GlobalScope.launch {
            while (this.isActive) {
                writeCurrentTime(file)
                delay(500)
            }
        }
    }

    private suspend fun writeCurrentTime(file: File) {
        withContext(Dispatchers.IO) {
            val tempFile = File(file.parent, "temp123.txt")
            FileWriter(tempFile).use { fileWriter ->
                LocalDateTime.now().let {
                    fileWriter.write("${dateFormatter.format(it)}  ${timeFormatter.format(it)}")
                }
            }
            Files.move(
                Paths.get(tempFile.absolutePath),
                Paths.get(file.absolutePath),
                StandardCopyOption.ATOMIC_MOVE
            )
        }
    }

}