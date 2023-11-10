package com.example.moontech.ui.screens.userauth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.PrimaryButton

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUp: () -> Unit,
    navigateToLoginScreen: () -> Unit
) = CenterScreen(modifier) {
    Text(text = "Sign Up screen")
    PrimaryButton(text = "Sign Up", onClick = signUp)
    PrimaryButton(text = "Already have an acccount? Log in", onClick = navigateToLoginScreen)
}