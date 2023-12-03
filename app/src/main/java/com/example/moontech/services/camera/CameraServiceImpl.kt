package com.example.moontech.services.camera

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
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.lib.streamingcamera.CameraXStreamingCamera
import com.example.moontech.lib.streamingcamera.StreamingCamera
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraServiceImpl() : LifecycleService(), CameraService {
    companion object {
        private const val TAG = "CameraServiceImpl"
    }

    private val binder: IBinder = LocalBinder()
    private lateinit var streamingCamera: StreamingCamera

    private val _serviceState = MutableStateFlow(CameraServiceState())
    override val serviceState = _serviceState.asStateFlow()

    override fun startStream(url: String, name: String) {
        Log.i(TAG, "startStream: ")
        _serviceState.updateConditionally({ !isStreaming }) {
            streamingCamera.startStream(rtmpUrl = url, onStreamFailed = {
                stopStreamAfterError(AppState.Error("Transmission error"))
            })
            it.copy(isStreaming = true, streamName = name)
        }
    }

    override fun stopStream() {
        _serviceState.updateConditionally({ isStreaming }) {
            streamingCamera.stopStream()
            it.copy(isStreaming = false)
        }
    }

    override fun startPreview(surfaceProvider: SurfaceProvider) {
        Log.i(TAG, "startPreview: ")
        _serviceState.updateConditionally({ !isPreview }) {
            streamingCamera.startPreview(surfaceProvider)
            it.copy(isPreview = true)
        }
    }

    override fun stopPreview() {
        Log.i(TAG, "stopPreview: ")
        _serviceState.updateConditionally({isPreview}) {
            streamingCamera.stopPreview()
            it.copy(isPreview = false)
        }
    }

    override fun closeServiceIfNotUsed() {
        Log.i(TAG, "closeServiceIfNotUsed: ")
        _serviceState.updateConditionally({!isPreview && !isStreaming}) {
            Log.i(TAG, "closeServiceIfNotUsed: stopping self")
            stopSelf()
            it.copy(isStreaming = false, isPreview = false)
        }
    }

    private fun stopStreamAfterError(error: AppState) {
        _serviceState.update { prevState ->
            if (prevState.isStreaming) {
                Log.i(TAG, "startStream: Stream failed")
                prevState.copy(isStreaming = false, streamError = error)
            } else {
                prevState
            }
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

    private fun <T> MutableStateFlow<T>.updateConditionally(
        condition: T.() -> Boolean,
        block: (T) -> T
    ) {
        update {
            if (condition(it)) {
                block(it)
            } else {
                it
            }
        }
    }
}