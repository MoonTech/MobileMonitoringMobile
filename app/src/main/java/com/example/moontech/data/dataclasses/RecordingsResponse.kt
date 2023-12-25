package com.example.moontech.data.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class RecordingsResponse(val recordings: List<Recording>)