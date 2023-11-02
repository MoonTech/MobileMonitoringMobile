package com.example.moontech.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.camera.core.Preview.SurfaceProvider
import androidx.lifecycle.LifecycleService
import com.example.moontech.lib.streamingcamera.CameraXStreamingCamera
import com.example.moontech.lib.streamingcamera.StreamingCamera

class CameraServiceImpl() : LifecycleService(), CameraService {
    companion object {
        private const val TAG = "CameraServiceImpl"
    }

    private val binder: IBinder = LocalBinder()
    private lateinit var streamingCamera: StreamingCamera

    override fun startStream(rtmpUrl: String) {
        Log.i(TAG, "startStream: ")
        streamingCamera.startStream(rtmpUrl)
    }

    override fun stopStream() {
        Log.i(TAG, "stopStream: ")
        streamingCamera.stopStream()
    }

    override fun startPreview(surfaceProvider: SurfaceProvider) {
        Log.i(TAG, "startPreview: ")
        return streamingCamera.startPreview(surfaceProvider)
    }

    override fun stopPreview() {
        Log.i(TAG, "stopPreview: ")
        streamingCamera.stopPreview()
    }

    // called once when service is created
    override fun onCreate() {
        Log.i(TAG, "onCreate: onCreate")
//        ServiceCompat.startForeground()
        streamingCamera = CameraXStreamingCamera(this, this)
        super.onCreate()
    }

    // called on start and restart
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    // called when client binds
    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "onBind: ")
        super.onBind(intent)
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): CameraService = this@CameraServiceImpl
    }
}