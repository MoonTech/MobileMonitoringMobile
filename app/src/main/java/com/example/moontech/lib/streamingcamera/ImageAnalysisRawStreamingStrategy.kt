package com.example.moontech.lib.streamingcamera

import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.moontech.lib.streamer.rtmp.FFmpegStreamCommand
import java.util.concurrent.Executors

class ImageAnalysisRawStreamingStrategy(): StreamingStrategy {

    private val executor = Executors.newSingleThreadExecutor()
    private var imageAnalysis: ImageAnalysis = ImageAnalysis.Builder().build()

    override fun init(
        processCameraProvider: ProcessCameraProvider,
        newFrameCallback: (byteArray: ByteArray) -> Unit
    ) {
        imageAnalysis = ImageAnalysis.Builder().build()
        imageAnalysis.setAnalyzer(executor) {

        }
    }

    override fun supportedFFmpegCommand(): FFmpegStreamCommand {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}