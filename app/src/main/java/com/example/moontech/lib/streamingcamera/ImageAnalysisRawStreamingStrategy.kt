package com.example.moontech.lib.streamingcamera

import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.moontech.lib.streamer.rtmp.StreamCommand
import java.nio.ByteBuffer
import java.nio.ReadOnlyBufferException
import java.util.concurrent.Executors
import kotlin.experimental.inv

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
                val outputConvertedBuffer = ByteBuffer.wrap(YUV_420_888toNV21(image))
                newFrameCallback.invoke(outputConvertedBuffer)
            }
            it.close()
        }
        return imageAnalysis
    }

    override fun supportedStreamCommand(): StreamCommand {
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

    private fun YUV_420_888toNV21(image: Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 4
        val nv21 = ByteArray(ySize + uvSize * 2)
        val yBuffer = image.planes[0].buffer // Y
        val uBuffer = image.planes[1].buffer // U
        val vBuffer = image.planes[2].buffer // V
        var rowStride = image.planes[0].rowStride
        assert(image.planes[0].pixelStride == 1)
        var pos = 0
        if (rowStride == width) { // likely
            yBuffer[nv21, 0, ySize]
            pos += ySize
        } else {
            var yBufferPos = -rowStride.toLong() // not an actual position
            while (pos < ySize) {
                yBufferPos += rowStride.toLong()
                yBuffer.position(yBufferPos.toInt())
                yBuffer[nv21, pos, width]
                pos += width
            }
        }
        rowStride = image.planes[2].rowStride
        val pixelStride = image.planes[2].pixelStride
        assert(rowStride == image.planes[1].rowStride)
        assert(pixelStride == image.planes[1].pixelStride)
        if (pixelStride == 2 && rowStride == width && uBuffer[0] == vBuffer[1]) {
            // maybe V an U planes overlap as per NV21, which means vBuffer[1] is alias of uBuffer[0]
            val savePixel = vBuffer[1]
            try {
                vBuffer.put(1, savePixel.inv() as Byte)
                if (uBuffer[0] == savePixel.inv() as Byte) {
                    vBuffer.put(1, savePixel)
                    vBuffer.position(0)
                    uBuffer.position(0)
                    vBuffer[nv21, ySize, 1]
                    uBuffer[nv21, ySize + 1, uBuffer.remaining()]
                    return nv21 // shortcut
                }
            } catch (ex: ReadOnlyBufferException) {
                // unfortunately, we cannot check if vBuffer and uBuffer overlap
            }

            // unfortunately, the check failed. We must save U and V pixel by pixel
            vBuffer.put(1, savePixel)
        }

        // other optimizations could check if (pixelStride == 1) or (pixelStride == 2),
        // but performance gain would be less significant
        for (row in 0 until height / 2) {
            for (col in 0 until width / 2) {
                val vuPos = col * pixelStride + row * rowStride
                nv21[pos++] = vBuffer[vuPos]
                nv21[pos++] = uBuffer[vuPos]
            }
        }
        return nv21
    }
}