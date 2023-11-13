package com.example.moontech.ui.screens.userauth

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.PrimaryButton

@Composable
fun AuthScreenBase(
    @StringRes firstButtonLabel: Int,
    @StringRes secondButtonLabel: Int,
    @StringRes firstTextFieldLabel: Int,
    @StringRes secondTextFieldLabel: Int,
    @StringRes screenLabel: Int,
    firstButtonAction: (username: String, password: String) -> Unit,
    secondButtonAction: () -> Unit,
    modifier: Modifier = Modifier,
) = CenterScreen(modifier) {
    CenterColumn(
        modifier = Modifier
            .widthIn(max = 300.dp)
            .padding(bottom = 30.dp)
    ) {
        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Spacer(modifier = Modifier.weight(1.5f))
        Text(
            text = stringResource(screenLabel),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 3.dp),
            style = MaterialTheme.typography.titleSmall
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(firstTextFieldLabel)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(secondTextFieldLabel)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryButton(
            text = stringResource(firstButtonLabel),
            onClick = { firstButtonAction(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = secondButtonAction,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
        ) {
            Text(text = stringResource(secondButtonLabel))
        }
    }
}