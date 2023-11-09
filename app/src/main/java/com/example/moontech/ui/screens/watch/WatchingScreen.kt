package com.example.moontech.ui.screens.watch

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun WatchingScreen(modifier: Modifier = Modifier, viewModel: AppViewModel) = CenterScreen(modifier) {
    val uiState by viewModel.uiState.collectAsState()
    CenterColumn {
        Text("Watching ${uiState.watchedRoom}")
    }
}