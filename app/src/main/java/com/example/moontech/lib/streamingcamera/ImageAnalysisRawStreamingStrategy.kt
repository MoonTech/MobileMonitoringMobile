package com.example.moontech.lib.streamingcamera

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.moontech.lib.streamer.rtmp.StreamCommand
import com.example.moontech.lib.utils.Yuv
import java.nio.ByteBuffer
import java.util.concurrent.Executors

private const val TAG = "ImageAnalysisRawStreamingStrategy"
class ImageAnalysisRawStreamingStrategy(): StreamingStrategy {

    private val executor = Executors.newSingleThreadExecutor()
    private var imageAnalysis: ImageAnalysis = ImageAnalysis.Builder().build()

    @OptIn(ExperimentalGetImage::class) override fun init(
        processCameraProvider: ProcessCameraProvider,
        newFrameCallback: (byteBuffer: ByteBuffer) -> Unit
    ): UseCase {
        imageAnalysis = ImageAnalysis.Builder().build()
        Log.i(TAG, "init: image analysis created")
        imageAnalysis.setAnalyzer(executor) {
            it.image?.let { image ->
                Log.i(TAG, "producing frame")
                Yuv.toBuffer(image).buffer
            }
        }
        return imageAnalysis
    }

    override fun supportedFFmpegCommand(): StreamCommand {
        return StreamCommand(
            inputFormat = "rawvideo",
            inputPixelFormat = "nv21",
            inputVideoSize = "640x480",
            inputFrameRate = 20,
            inputUrl = "inputUtl",
            encoder = "libx264",
            encoderSettings = "-profile baseline -preset veryfast -pix_fmt nv21"
        )
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}