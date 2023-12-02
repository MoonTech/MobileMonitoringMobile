package com.example.moontech.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.camera.core.Preview.SurfaceProvider
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.moontech.R
import com.example.moontech.data.dataclasses.AppError
import com.example.moontech.lib.streamingcamera.CameraXStreamingCamera
import com.example.moontech.lib.streamingcamera.StreamingCamera
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraServiceImpl() : LifecycleService(), CameraService {
    companion object {
        private const val TAG = "CameraServiceImpl"
    }

    private val binder: IBinder = LocalBinder()
    private lateinit var streamingCamera: StreamingCamera

    private val _isStreaming = MutableStateFlow(false)
    private val _isPreview = MutableStateFlow(false)
    private val _streamError = MutableStateFlow<AppError>(AppError.Empty())

    override val isStreaming = _isStreaming.asStateFlow()
    override val isPreview = _isPreview.asStateFlow()
    override val streamError = _streamError.asStateFlow()

    override fun startStream(rtmpUrl: String) {
        // TODO: Add error handling ex. invalid url
        Log.i(TAG, "startStream: ")
        if (_isStreaming.compareAndSet(expect = false, update = true)) {
            streamingCamera.startStream(rtmpUrl = rtmpUrl, onStreamFailed = {
                _isStreaming.compareAndSet(expect = true, update = false)
                Log.i(TAG, "startStream: Stream failed")
                _streamError.tryEmit(AppError.Error("Transmission error"))
            })
        }
    }

    override fun stopStream() {
        Log.i(TAG, "stopStream: ")
        if (_isStreaming.compareAndSet(expect = true, update = false)) {
            streamingCamera.stopStream()
        }
    }

    override fun startPreview(surfaceProvider: SurfaceProvider) {
        Log.i(TAG, "startPreview: ")
        if (_isPreview.compareAndSet(expect = false, update = true)) {
            return streamingCamera.startPreview(surfaceProvider)
        }
    }

    override fun stopPreview() {
        Log.i(TAG, "stopPreview: ")
        if (_isPreview.compareAndSet(expect = true, update = false)) {
            streamingCamera.stopPreview()
        }
    }

    override fun closeServiceIfNotUsed() {
        Log.i(TAG, "closeServiceIfNotUsed: ")
        if (!isPreview.value && !isStreaming.value) {
            Log.i(TAG, "closeServiceIfNotUsed: stopping self")
            stopSelf()
        }
    }

    // called once when service is created
    override fun onCreate() {
        Log.i(TAG, "onCreate: onCreate")
        startForeground()
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

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnbind: ")
        closeServiceIfNotUsed()
        return true
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy: destroying")
        super.onDestroy()
    }

    // TODO: Refactor notification
    private fun startForeground() {
        var channelId = createNotificationChannel("camera_service", "Camera Service")
        val notification = NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setContentTitle("Camera is active")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(JobInfo.PRIORITY_MAX)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    inner class LocalBinder : Binder() {
        fun getService(): CameraService = this@CameraServiceImpl
    }
}