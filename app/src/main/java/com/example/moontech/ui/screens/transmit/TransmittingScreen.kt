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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moontech.data.dataclasses.RoomCamera
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.screens.common.RoomType

private const val TAG = "TransmittingScreen"

@Composable
fun TransmittingScreen(
    roomCamera: RoomCamera,
    modifier: Modifier = Modifier,
    startPreview: (surfaceProvider: SurfaceProvider) -> Unit,
    startStream: (url: String) -> Unit,
    isStreaming: Boolean,
    stopPreview: () -> Unit,
    stopStream: () -> Unit
) = CenterScreen(modifier) {
    val stopPreviewRemembered = rememberUpdatedState(newValue = stopPreview)
    DisposableEffect(key1 = true) {
        onDispose {
            Log.i(TAG, "TransmittingScreen: Stopping preview")
            stopPreviewRemembered.value()
        }
    }
    CenterColumn {
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
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
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
                        startStream(roomCamera.url)
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
            Text(
                text = "Camera in room: ${roomCamera.code}",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp)
            )
        }
    }
}


@Preview
@Composable
fun TransmittingScreenPreview() {
    Surface {
        TransmittingScreen(
            roomCamera = RoomCamera("123", "123", "123", RoomType.EXTERNAL),
            startPreview = {},
            startStream = {},
            stopPreview = {},
            stopStream = {},
            isStreaming = false
        )
    }
}