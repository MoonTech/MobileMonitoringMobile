package com.example.moontech.ui.screens.myrooms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun SplashScreen(
    viewModel: AppViewModel,
    navigateToLoginScreen: () -> Unit,
    navigateToMainScreen: () -> Unit,
    modifier: Modifier = Modifier
) = CenterScreen(modifier) {
    val isLoggedIn by viewModel.loggedInState.collectAsState()
    val onNavigateToLoginScreen = rememberUpdatedState(newValue = navigateToLoginScreen)
    val onNavigateToMainScreen = rememberUpdatedState(newValue = navigateToMainScreen)

    LaunchedEffect(true) {
        when (isLoggedIn) {
            true -> onNavigateToMainScreen.value()
            false -> onNavigateToLoginScreen.value()
            null -> {  }
        }
    }
}