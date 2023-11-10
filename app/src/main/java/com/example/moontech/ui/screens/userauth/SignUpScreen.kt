package com.example.moontech.ui.screens.userauth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.R

@Composable
fun SignUpScreen(
    signUp: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier
) = AuthScreenBase(
    firstButtonLabel = R.string.sign_up,
    secondButtonLabel = R.string.log_into_existing_account,
    firstTextFieldLabel = R.string.username,
    secondTextFieldLabel = R.string.password,
    screenLabel = R.string.sign_up,
    firstButtonAction = signUp,
    secondButtonAction = navigateToLoginScreen,
    modifier = modifier
)