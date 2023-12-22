package com.example.moontech.ui.screens.transmit

import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moontech.data.dataclasses.RoomCamera
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.TransmittingControls
import com.example.moontech.ui.components.TransmittingInfoPanel

private const val TAG = "TransmittingScreen"

@Composable
fun TransmittingScreen(
    roomCamera: RoomCamera?,
    modifier: Modifier = Modifier,
    startPreview: (surfaceProvider: SurfaceProvider) -> Unit,
    startStream: (roomCamera: RoomCamera) -> Unit,
    isStreaming: Boolean,
    stopPreview: () -> Unit,
    stopStream: () -> Unit,
    selectCamera: () -> Unit
) = CenterScreen(modifier) {
    // TODO: Fix stopping preview
    var previewStopped by remember{mutableStateOf(false)}
    DisposableEffect(key1 = true) {
        onDispose {
            Log.i(TAG, "TransmittingScreen: Stopping preview")
            if (!previewStopped) {
                stopPreview()
            }
        }
    }
    CenterColumn {
        TransmittingInfoPanel(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 2.dp, top = 2.dp),
            isStreaming = isStreaming,
            roomCamera = roomCamera,
            onSwitchRoom = { selectCamera() }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
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
                        Log.i(TAG, "TransmittingScreen: PreviewView created")
                    }
                },
                onRelease = { Log.i(TAG, "TransmittingScreen: on release") }
            )
            TransmittingControls(
                roomCamera = roomCamera,
                isStreaming = isStreaming,
                onStartStream = {
                    roomCamera?.let { startStream(it) }
                },
                onStopStream = {
                    stopStream()
                }
            )
        }
    }
}