package com.example.moontech.lib.streamingcamera

import java.io.Closeable
import java.nio.ByteBuffer

interface FrameMediator: Closeable {
    fun onFrameProduced(byteBuffer: ByteBuffer)
}