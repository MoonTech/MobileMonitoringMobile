package com.example.moontech.lib.streamingcamera

import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

private const val TAG = "PipeFrameMediator"
class PipeFrameMediator(private val outputPipe: String): FrameMediator {
    private val outputStream: FileOutputStream = File(outputPipe).outputStream()
    // TODO: Count and display FPS
    @Volatile
    var frameCount = 0


    override fun onFrameProduced(byteBuffer: ByteBuffer) {
        if (outputStream.channel.isOpen)
            try {
                outputStream.write(byteBuffer.array())
            } catch (e: Exception) {
                close()
            }
        }

    override fun close() {
        outputStream.close()
    }
}