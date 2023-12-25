package com.example.moontech.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> MenuDrawer(
    modifier: Modifier = Modifier,
    items: List<T>,
    header: @Composable (closeMenu: () -> Unit) -> Unit,
    itemContent: @Composable (closeMenu: () -> Unit, T) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    IconButton(modifier = modifier, onClick = {
        showMenu = true
    }) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier.widthIn(min = 200.dp)
        ) {
            header { showMenu = false }
            for (item in items) {
                itemContent({ showMenu = false }, item)
            }
        }
    }
}