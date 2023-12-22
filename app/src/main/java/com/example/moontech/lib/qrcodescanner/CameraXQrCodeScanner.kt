package com.example.moontech.lib.qrcodescanner

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class CameraXQrCodeScanner(
    private val cameraProvider: ProcessCameraProvider,
    private val lifecycleOwner: LifecycleOwner
) : QrCodeScanner {
    override var onCodeScanned: (code: String) -> Unit = {  }
    private val executor = Executors.newSingleThreadExecutor()
    private val useCase = ImageAnalysis.Builder().build()
    private val barcodeOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(barcodeOptions)

    companion object {
        private const val TAG = "CameraXQrCodeScanner"
    }

    override fun init() {
        useCase.setAnalyzer(executor) { checkQRCode(it) }
        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, useCase)
    }

    override fun close() {
        cameraProvider.unbind(useCase)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun checkQRCode(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            var inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { qrCodes ->
                    if (qrCodes.isEmpty()) {
                        Log.i(TAG, "No qr codes found")
                    }
                    qrCodes.forEach { qrCode ->
                        qrCode.rawValue?.let { codeString ->
                            onCodeScanned(codeString)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "checkQRCode: ", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }

    }
}