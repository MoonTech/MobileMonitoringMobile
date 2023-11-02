package com.example.moontech.ui.screens.transmit

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moontech.services.CameraService
import com.example.moontech.services.CameraServiceImpl
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.viewmodel.AppViewModel

private const val TAG = "TransmittingScreen"
@Composable
fun TransmittingScreen(modifier: Modifier = Modifier, viewModel: AppViewModel) =
    CenterScreen(modifier) {
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current

        val serviceState = produceState<CameraService?>(initialValue = null) {
            val intent = Intent(context, CameraServiceImpl::class.java)
//            context.startForegroundService(intent)
            val connection: ServiceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    value = (service as CameraServiceImpl.LocalBinder).getService()
                    Log.i(TAG, "onServiceConnected: service produced $value")
                }
                override fun onServiceDisconnected(name: ComponentName?) { }
            }
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            Log.i(TAG, "TransmittingScreen: service bind")
            awaitDispose {
                context.unbindService(connection)
            }
        }

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
                            serviceState.value?.startPreview(this.surfaceProvider)
                            Log.i(TAG, "TransmittingScreen: PreviewView created")
                        }
                    },
                    update = {
                        Log.i(TAG, "TransmittingScreen: PreviewView updated")
                        serviceState.value?.startPreview(it.surfaceProvider)
                    }
                )
                ElevatedButton(
                    onClick = { serviceState.value?.stopPreview() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.FiberManualRecord,
                        contentDescription = "Record",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Start transmission")
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
        TransmittingScreen(viewModel = viewModel())
    }
}