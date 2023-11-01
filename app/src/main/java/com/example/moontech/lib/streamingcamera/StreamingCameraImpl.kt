package com.example.moontech.lib.streamingcamera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class StreamingCameraImpl(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
): StreamingCamera {
    private var preview: Preview = Preview.Builder().build()
    private var imageAnalysis: ImageAnalysis? = null
    private val cameraProvider: LiveData<ProcessCameraProvider> by lazy {
        MutableLiveData<ProcessCameraProvider>().apply {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                Log.i(TAG, "CameraProvider: provided camera provider to live data")
                value = cameraProviderFuture.get()
            }, ContextCompat.getMainExecutor(context))
        }
    }

    companion object {
        private const val TAG = "StreamingCameraImpl"
    }

    override fun startStream(rtmpUrl: String) {
        Log.i(TAG, "startStream: ")
        TODO("Not yet implemented")
    }

    override fun stopStream() {
        Log.i(TAG, "stopStream: ")
        TODO("Not yet implemented")
    }

    override fun startPreview(): Preview {
        Log.i(TAG, "startPreview: preview call requested")
        withCameraProvider {
            Log.i(TAG, "startPreview: preview call started")
            bindToLifecycle(preview)
        }
        return preview
    }

    override fun stopPreview() {
        Log.i(TAG, "stopPreview: ")
        withCameraProvider { unbind(preview) }
    }

    private fun ProcessCameraProvider.bindToLifecycle(vararg useCase: UseCase) {
        bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, *useCase)
    }

    private fun withCameraProvider(block: ProcessCameraProvider.() -> Unit) {
        cameraProvider.observeOnce(block)
    }

    private fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
        observeForever(object: Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }
}