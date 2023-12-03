package com.example.moontech.services

import com.example.moontech.data.dataclasses.AppError

data class CameraServiceState(
    val isStreaming: Boolean = false,
    val isPreview: Boolean = false,
    val streamName: String? = null,
    val streamError: AppError = AppError.Empty()
)