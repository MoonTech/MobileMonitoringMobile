package com.example.moontech.ui.screens.transmit

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moontech.data.dataclasses.QrCodeContent
import com.example.moontech.ui.components.CenterScreen

@Composable
fun ScanQRCodeScreen(
    modifier: Modifier = Modifier,
    startPreview: (surfaceProvider: Preview.SurfaceProvider) -> Unit,
    stopPreview: () -> Unit,
    startScanning: () -> Unit,
    stopScanning: () -> Unit,
    addCameraToRoom: (QrCodeContent) -> Unit,
    qrCodeContent: QrCodeContent?
) = CenterScreen(modifier) {
    var scannedQrCodeContent by remember { mutableStateOf<QrCodeContent?>(null) }
    var showQrCodeDialog by remember { mutableStateOf(false) }

    DisposableEffect(true) {
        startScanning()
        onDispose {
            stopScanning()
            stopPreview()
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    startPreview(this.surfaceProvider)
                }
            }
        )
        ElevatedButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            onClick = {
            scannedQrCodeContent = qrCodeContent
            showQrCodeDialog = true
        }) {
            Text(text = "Scan")
        }
        if (showQrCodeDialog && scannedQrCodeContent != null) {
            AlertDialog(
                onDismissRequest = {
                    showQrCodeDialog = false
                },
                text = { Text(text = "Do you want to add camera to room ${scannedQrCodeContent?.roomCode}?") },
                confirmButton = {
                    TextButton(onClick = {
                        showQrCodeDialog = false
                        scannedQrCodeContent?.let(addCameraToRoom)
                    }) {
                        Text(text = "Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showQrCodeDialog = false }) {
                        Text(text = "Cancel")
                    }
                })
        }
    }

}