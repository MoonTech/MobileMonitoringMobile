package com.example.moontech.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.data.dataclasses.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissableRoomTile(
    room: ObjectWithRoomCode,
    modifier: Modifier = Modifier,
    onDismiss: (room: ObjectWithRoomCode) -> Unit
) {
    val dismissState = rememberDismissState(
        positionalThreshold = { _ -> 128.dp.toPx() },
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                onDismiss(room)
            }
            true
        }
    )
    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        background = {
            Row(
                Modifier
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier.padding(end = 8.dp)
                )
            }
        }, dismissContent = {
            RoomTile(
                room = room,
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
            )
        },
        directions = setOf(DismissDirection.EndToStart)
    )
}

@Composable
@Preview
fun Preview() {
    val room = Room("Test room 1")
    DismissableRoomTile(room = room, onDismiss = { })
}