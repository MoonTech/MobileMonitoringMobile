package com.example.moontech.lib.streamer.rtmp

data class StreamCommand(
    val inputFormat: String,
    val additionalInputParameters: String = "",
    val filters: String = "",
    val inputUrl: String,
    val encoder: String,
    val encoderSettings: String
) {
    override fun toString(): String {
        return "-f $inputFormat $additionalInputParameters -i $inputUrl -c:v $encoder $encoderSettings -filter_complex \"$filters".trimIndent()
    }
}