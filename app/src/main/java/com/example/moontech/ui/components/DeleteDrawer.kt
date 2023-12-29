package com.example.moontech.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DeleteDrawer(modifier: Modifier = Modifier, onDelete: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.MoreHoriz,
            contentDescription = "Setting",
            modifier = Modifier
                .clip(shape = CircleShape)
                .clickable {
                    showMenu = true
                }
                .fillMaxHeight()
        )
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            Text(text = "Delete", modifier = Modifier
                .clickable {
                    onDelete()
                }
                .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
            )
        }
    }
}