package com.example.moontech.lib.streamer

interface Streamer {

    /**
     * @param url Url to stream to, format depends omn implementation.
     * @return Path to pipe consuming video frames to stream.
     */
    fun startStream(url: String): String

    fun endStream(url: String)
}