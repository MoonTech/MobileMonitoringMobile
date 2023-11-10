package com.example.moontech.ui.screens.userauth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.PrimaryButton

@Composable
fun LoginScreen(
    navigateToSignUpScreen: () -> Unit,
    logIn: () -> Unit,
    modifier: Modifier = Modifier
) = CenterScreen(modifier) {
    Text(text = "Login screen")
    PrimaryButton(text = "Log in", onClick = logIn)
    PrimaryButton(text = "No account? Sign Up", onClick = navigateToSignUpScreen)
}