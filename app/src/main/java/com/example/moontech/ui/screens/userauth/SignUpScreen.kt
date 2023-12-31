package com.example.moontech.ui.screens.userauth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moontech.R
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.ui.screens.base.AuthScreenBase

@Composable
fun SignUpScreen(
    signUp: (username: String, password: String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    emitError: (error: AppState.Error) -> Unit,
    modifier: Modifier = Modifier
) = AuthScreenBase(
    firstButtonLabel = R.string.sign_up,
    secondButtonLabel = R.string.log_into_existing_account,
    firstTextFieldLabel = R.string.username,
    secondTextFieldLabel = R.string.password,
    screenLabel = R.string.sign_up,
    firstButtonAction = signUp,
    secondButtonAction = navigateToLoginScreen,
    emitError = emitError,
    modifier = modifier
)