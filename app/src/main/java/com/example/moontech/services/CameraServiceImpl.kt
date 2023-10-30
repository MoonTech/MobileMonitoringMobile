package com.example.moontech.services

import android.content.Intent
import android.os.IBinder
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleService

class CameraServiceImpl() : LifecycleService(), CameraService {

    private var preview: Preview? = null
    private var imageAnalysis: ImageAnalysis? = null
    override fun startStream(rtmpUrl: String) {
        TODO("Not yet implemented")
    }

    override fun stopStream() {
        TODO("Not yet implemented")
    }

    // called once when service is created
    override fun onCreate() {
        preview = Preview.Builder()
            .build()
        super.onCreate()
    }

    // called on start and restart
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    // called when client binds
    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }
}