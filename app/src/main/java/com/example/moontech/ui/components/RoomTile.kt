package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.ObjectWithRoomCode

@Composable
fun RoomTile(room: ObjectWithRoomCode, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = room.code, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
    }
}