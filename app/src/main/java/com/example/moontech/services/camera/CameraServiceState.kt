package com.example.moontech.services.camera

import com.example.moontech.data.dataclasses.AppState

data class CameraServiceState(
    val isStreaming: Boolean = false,
    val isPreview: Boolean = false,
    val streamName: String? = null,
    val streamError: AppState = AppState.Empty(),
    val lastQrCodeContent: String? = null
)