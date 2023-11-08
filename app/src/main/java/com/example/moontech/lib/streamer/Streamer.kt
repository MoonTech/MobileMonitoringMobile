package com.example.moontech.lib.streamer

import com.example.moontech.lib.streamer.rtmp.StreamCommand

interface Streamer {

    /**
     * @param url Url to stream to, format depends omn implementation.
     * @param streamCommand Command describing input video and encoder settings
     * @return Path to pipe consuming video frames to stream.
     */
    fun startStream(url: String, streamCommand: StreamCommand): String

    fun endStream(url: String)

    fun endAllStreams()
}