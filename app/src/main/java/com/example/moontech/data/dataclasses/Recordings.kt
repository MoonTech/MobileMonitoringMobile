package com.example.moontech.data.dataclasses

data class Recordings(override val code: String, val recordings: List<Recording>): ObjectWithRoomCode {
}