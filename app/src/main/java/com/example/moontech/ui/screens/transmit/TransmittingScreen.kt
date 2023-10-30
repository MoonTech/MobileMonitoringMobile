package com.example.moontech.ui.screens.transmit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun TransmittingScreen(modifier: Modifier = Modifier, viewModel: AppViewModel) =
    CenterScreen(modifier) {
        val uiState by viewModel.uiState.collectAsState()
        CenterColumn {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black)
                    .weight(1f)
            ) {
                ElevatedButton(
                    onClick = { },
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