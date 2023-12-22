package com.example.moontech.lib.streamingcamera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.moontech.lib.qrcodescanner.CameraXQrCodeScanner
import com.example.moontech.lib.qrcodescanner.QrCodeScanner
import com.example.moontech.lib.streamer.Streamer
import com.example.moontech.lib.streamer.rtmp.RtmpStreamer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CameraXStreamingCamera(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : StreamingCamera {
    private var preview: Preview? = null

    private var streamUseCase: UseCase? = null
    private var streamMediator: FrameMediator? = null
    private var streamer: Streamer = RtmpStreamer(context)
    private var streamingStrategy: StreamingStrategy = ImageAnalysisRawStreamingStrategy()
    private val cameraProvider = ProcessCameraProvider.getInstance(context).get()
    private val qrCodeScanner: QrCodeScanner = CameraXQrCodeScanner(cameraProvider, lifecycleOwner)
    override var onQrCodeScanned: (String) -> Unit = {}

    companion object {
        private const val TAG = "CameraXStreamingCamera"
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun startStream(
        rtmpUrl: String,
        onStreamFailed: () -> Unit
    ) {
        Log.i(TAG, "startStream: ")
        withRotationDegrees { rotationDegrees ->
            val streamCommand = streamingStrategy.supportedStreamCommand()
                .copy(filters = "rotate=$rotationDegrees * (PI/180), ")

            val pipe = streamer.startStream(
                url = rtmpUrl,
                streamCommand = streamCommand
            ) {
                onStreamFailed()
                GlobalScope.launch(Dispatchers.Main) {
                    stopStream()
                }
            }
            streamMediator = PipeFrameMediator(pipe)
            streamUseCase = streamingStrategy.init(cameraProvider) { buffer ->
                streamMediator?.onFrameProduced(buffer)
            }.also {
                cameraProvider.bindToLifecycle(it)
            }
        }

    }

    override fun stopStream() {
        Log.i(TAG, "stopStream: ")
        withCameraProvider {
            unbind(streamUseCase)
            streamMediator?.close()
            streamer.endAllStreams()
            streamingStrategy.close()
        }
    }

    override fun startPreview(surfaceProvider: SurfaceProvider) {
        Log.i(TAG, "startPreview: preview call requested")
        withCameraProvider {
            Log.i(TAG, "startPreview: preview call started")
            if (preview == null) {
                preview = Preview.Builder().build().apply {
                    bindToLifecycle(this)
                }
            }
            preview?.setSurfaceProvider(surfaceProvider)
        }
    }

    override fun stopPreview() {
        Log.i(TAG, "stopPreview: ")
        withCameraProvider {
            preview?.also {
                it.setSurfaceProvider(null)
            }
        }
    }

    override fun startQrCodeScanner() {
        qrCodeScanner.onCodeScanned = onQrCodeScanned
        qrCodeScanner.init()
    }

    override fun stopQrCodeScanner() {
        qrCodeScanner.close()
    }

    private fun ProcessCameraProvider.bindToLifecycle(vararg useCase: UseCase) {
        bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, *useCase)
    }

    private fun withCameraProvider(block: ProcessCameraProvider.() -> Unit) {
        cameraProvider.block()
    }

    private fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
        observeForever(object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

    private fun imageCaptor(block: (rotationDegrees: Int) -> Unit): OnImageCapturedCallback {
        return object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                block(image.imageInfo.rotationDegrees)
                image.close()
            }
        }
    }

    private fun withRotationDegrees(block: (rotationDegrees: Int) -> Unit) {
        ImageCapture.Builder().build().also {
            cameraProvider.bindToLifecycle(it)
            it.takePicture(
                ContextCompat.getMainExecutor(context),
                imageCaptor { rotationDegrees ->
                    block(rotationDegrees)
                    cameraProvider.unbind(it)
                }
            )
        }
    }
}