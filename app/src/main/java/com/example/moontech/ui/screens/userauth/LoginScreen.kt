package com.example.moontech.ui.screens.userauth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.R
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.ui.screens.base.AuthScreenBase

@Composable
fun LoginScreen(
    logIn: (username: String, password: String) -> Unit,
    navigateToSignUpScreen: () -> Unit,
    emitError: (error: AppState.Error) -> Unit,
    modifier: Modifier = Modifier
) = AuthScreenBase(
    firstButtonLabel = R.string.log_in,
    secondButtonLabel = R.string.create_account,
    firstTextFieldLabel = R.string.username,
    secondTextFieldLabel = R.string.password,
    screenLabel = R.string.log_in,
    firstButtonAction = logIn,
    secondButtonAction = navigateToSignUpScreen,
    emitError = emitError,
    modifier = modifier
)