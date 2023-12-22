package com.example.moontech.lib.qrcodescanner

interface QrCodeScanner {
    var onCodeScanned: (code: String) -> Unit
    fun init()
    fun close()
}