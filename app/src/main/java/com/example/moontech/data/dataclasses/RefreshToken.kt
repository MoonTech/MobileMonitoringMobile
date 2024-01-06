package com.example.moontech.data.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(@SerialName("Token") val token: String)