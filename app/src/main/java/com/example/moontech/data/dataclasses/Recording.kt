package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class Recording(val name: String, val url: String) {
}