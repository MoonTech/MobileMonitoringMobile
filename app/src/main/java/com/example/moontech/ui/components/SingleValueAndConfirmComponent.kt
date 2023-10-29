package com.example.moontech.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SingleValueAndConfirmComponent(
    value: String?,
    onValueChanged: (value: String) -> Unit,
    confirm: () -> Unit,
    @StringRes valueLabel: Int,
    @StringRes confirmLabel: Int
) {
    TextField(
        value = value ?: "",
        onValueChange = onValueChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        label = { Text(text = stringResource(valueLabel)) })
    PrimaryButton(
        text = stringResource(confirmLabel),
        onClick = confirm,
        modifier = Modifier.fillMaxWidth()
    )
}