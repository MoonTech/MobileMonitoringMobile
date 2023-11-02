package com.example.moontech.lib.streamingcamera

import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.moontech.lib.streamer.rtmp.FFmpegStreamCommand

interface StreamingStrategy {
    fun init(processCameraProvider: ProcessCameraProvider, newFrameCallback: (byteArray: ByteArray) -> Unit)
    fun supportedFFmpegCommand(): FFmpegStreamCommand
    fun close()
}