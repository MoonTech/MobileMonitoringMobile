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
import com.example.moontech.lib.streamingcamera.CameraXStreamingCamera
import com.example.moontech.lib.streamingcamera.StreamingCamera

class CameraServiceImpl() : LifecycleService(), CameraService {
    companion object {
        private const val TAG = "CameraServiceImpl"
    }

    private val binder: IBinder = LocalBinder()
    private lateinit var streamingCamera: StreamingCamera
    override var isStreaming = false
        private set
    override var isPreview = false
        private set

    override fun startStream(rtmpUrl: String) {
        Log.i(TAG, "startStream: ")
        if (!isStreaming) {
            streamingCamera.startStream(rtmpUrl)
            isStreaming = true
        }
    }

    override fun stopStream() {
        Log.i(TAG, "stopStream: ")
        if (isStreaming) {
            streamingCamera.stopStream()
            isStreaming = false
        }
    }

    override fun startPreview(surfaceProvider: SurfaceProvider) {
        Log.i(TAG, "startPreview: ")
        if (!isPreview) {
            return streamingCamera.startPreview(surfaceProvider).also {
                isPreview = true
            }
        }
    }

    override fun stopPreview() {
        Log.i(TAG, "stopPreview: ")
        if (isPreview) {
            streamingCamera.stopPreview()
            isPreview = false
        }
    }

    override fun closeServiceIfNotUsed() {
        Log.i(TAG, "closeServiceIfNotUsed: ")
        if (!isPreview && !isStreaming) {
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
        closeServiceIfNotUsed()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy: destroying")
        super.onDestroy()
    }

    // TODO: Refactor notification
    private fun startForeground() {
        var channelId = createNotificationChannel("camera_service", "Camera Service")
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
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