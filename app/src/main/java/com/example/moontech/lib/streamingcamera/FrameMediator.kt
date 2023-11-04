package com.example.moontech.lib.streamingcamera

import java.nio.ByteBuffer

interface FrameMediator {
    fun onFrameProduced(byteBuffer: ByteBuffer)
    fun close()
}