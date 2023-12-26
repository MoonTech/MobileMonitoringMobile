package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class QrCodeContent(val roomName: String, val token: String) {
}