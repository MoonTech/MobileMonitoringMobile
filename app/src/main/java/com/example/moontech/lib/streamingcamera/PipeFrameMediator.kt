package com.example.moontech.lib.streamingcamera

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

private const val TAG = "PipeFrameMediator"
class PipeFrameMediator(private val outputPipe: String): FrameMediator {
    val outputStream: FileOutputStream = File(outputPipe).outputStream()

    override fun onFrameProduced(byteBuffer: ByteBuffer) {
        Log.i(TAG, "onFrameProduced: writing frame")
        outputStream.write(byteBuffer.array())
    }

    override fun close() {
        outputStream.close()
    }
}