package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val login: String,
    val password: String
)