package com.example.moontech.lib.streamer.rtmp

data class StreamCommand(
    val inputFormat: String,
    val inputPixelFormat: String,
    val inputVideoSize: String,
    val inputFrameRate: Int,
    val inputUrl: String,
    val encoder: String,
    val encoderSettings: String
) {
    override fun toString(): String {
        return """
            -f $inputFormat 
            -pixel_format $inputPixelFormat 
            -video_size $inputVideoSize 
            -framerate $inputFrameRate 
            -i $inputUrl
            -c:v $encoder $encoderSettings
        """.trimIndent()
    }
}