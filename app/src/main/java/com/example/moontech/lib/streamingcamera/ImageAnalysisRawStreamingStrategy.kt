package com.example.moontech.lib.streamingcamera

import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.util.Log
import android.util.Range
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.camera.core.resolutionselector.ResolutionFilter
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.moontech.lib.streamer.rtmp.StreamCommand
import java.nio.ByteBuffer
import java.nio.ReadOnlyBufferException
import java.util.concurrent.Executors
import kotlin.experimental.inv


private const val TAG = "ImageAnalysisRawStreamingStrategy"

@OptIn(ExperimentalCamera2Interop::class)
class ImageAnalysisRawStreamingStrategy() : StreamingStrategy {

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var imageAnalysis: ImageAnalysis

    init {
        val builder = ImageAnalysis.Builder()
        val ext: Camera2Interop.Extender<*> = Camera2Interop.Extender(builder)
        ext.setCaptureRequestOption(
            CaptureRequest.CONTROL_AE_MODE,
            CaptureRequest.CONTROL_AE_MODE_OFF
        )
        ext.setCaptureRequestOption(
            CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
            Range(25, 35)
        )
        builder.setResolutionSelector(
            ResolutionSelector.Builder().setResolutionFilter(
                ResolutionFilter { supportedSizes, rotationDegrees -> supportedSizes.filter { size -> size.height == 640 || size.width == 640 } })
                .build()
        )
        val imageAnalysis = builder.build()
    }

    @OptIn(ExperimentalGetImage::class)
    override fun init(
        processCameraProvider: ProcessCameraProvider,
        newFrameCallback: (byteBuffer: ByteBuffer, width: Int, height: Int, rotationDegrees: Int) -> Unit
    ): UseCase {
        imageAnalysis = ImageAnalysis.Builder().build()
        Log.i(TAG, "init: image analysis created")
        imageAnalysis.setAnalyzer(executor) {
            it.image?.let { image ->
                val outputConvertedBuffer = ByteBuffer.wrap(YUV_420_888toNV21(image))
                newFrameCallback.invoke(outputConvertedBuffer, it.width, it.height, it.imageInfo.rotationDegrees)
            }
            it.close()
        }
        return imageAnalysis
    }

    override fun supportedStreamCommand(width: Int, height: Int): StreamCommand {
        return StreamCommand(
            inputFormat = "rawvideo",
            additionalInputParameters = "-pixel_format nv21 -video_size ${width}x${height} -framerate 35 -use_wallclock_as_timestamps 1 ",
            inputUrl = "inputUrl",
            encoder = "libx264",
            encoderSettings = "-profile:v baseline -x264-params keyint=105:scenecut=0 -preset veryfast",
        )
    }

    override fun close() {
        // Not needed here
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
                vBuffer.put(1, savePixel.inv())
                if (uBuffer[0] == savePixel.inv()) {
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