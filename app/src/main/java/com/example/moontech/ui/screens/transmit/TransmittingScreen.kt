package com.example.moontech.ui.screens.transmit

import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.viewmodel.UiState

private const val TAG = "TransmittingScreen"

@Composable
fun TransmittingScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    startPreview: (surfaceProvider: SurfaceProvider) -> Unit,
    startStream: (url: String) -> Unit,
    isPreview: Boolean,
    isStreaming: Boolean,
    stopPreview: () -> Unit,
    stopStream: () -> Unit
) = CenterScreen(modifier) {
    //TODO: utilise stopPreview

    CenterColumn {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
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
                        setBackgroundColor(android.graphics.Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                        startPreview(this.surfaceProvider)
                        Log.i(TAG, "TransmittingScreen: PreviewView created")
                    }
                }
            )
            ElevatedButton(
                onClick = {
                    if (isStreaming) {
                        stopStream()
                    } else {
                        startStream("rtmp://192.168.0.109:1935/live/test-camera")
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FiberManualRecord,
                    contentDescription = "Record",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = if (isStreaming) "Stop transmission" else "Start transmission")
            }
        }
        Row(modifier.weight(0.2f)) {
            Text("Transmitting ${uiState.transmittingRoom}")
        }
    }
}


@Preview
@Composable
fun TransmittingScreenPreview() {
    Surface {
        TransmittingScreen(
            uiState = UiState(),
            startPreview = {},
            startStream = {},
            stopPreview = {},
            stopStream = {},
            isPreview = false,
            isStreaming = false
        )
    }
}