package com.example.moontech.lib.streamingcamera

import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.moontech.lib.streamer.rtmp.StreamCommand
import java.nio.ByteBuffer

interface StreamingStrategy {
    fun init(processCameraProvider: ProcessCameraProvider, newFrameCallback: (byteBuffer: ByteBuffer) -> Unit): UseCase
    fun supportedStreamCommand(width: Int, height: Int): StreamCommand
    fun close()
}