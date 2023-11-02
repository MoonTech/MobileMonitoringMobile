package com.example.moontech.lib.streamer.rtmp

class FFmpegStreamCommand(
    val inputFormat: String,
    val inputPixelFormat: String,
    val inputVideoSize: String,
    val inputFrameRate: Int,
    val inputUrl: String,
    val encoder: String,
    val encoderSettings: String,
    val outputFormat: String,
    val outputUrl: String
) {
    override fun toString(): String {
        return """
            -f $inputFormat 
            -pixel_format $inputPixelFormat 
            -video_size $inputVideoSize 
            -framerate $inputFrameRate 
            -i $inputUrl
            -c:v $encoder $encoderSettings
            -f $outputFormat $outputUrl
        """.trimIndent()
    }
}