package com.example.moontech.ui.components.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.data.dataclasses.Room
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.DismissableRoomTile
import com.example.moontech.ui.components.RoomTile

@Composable
fun RoomListBase(
    rooms: List<ObjectWithRoomCode>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: ((room: ObjectWithRoomCode) -> Unit)? = null
) =
    CenterColumn(modifier = modifier, verticalArrangement = Arrangement.Top) {
        val listState = remember { mutableStateListOf<ObjectWithRoomCode>() }
        listState.clear()
        listState.addAll(rooms)
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = listState,
                key = { it.code },
                contentType = { Room::class.java }
            ) {
                val room by rememberUpdatedState(it)
                if (onDelete != null) {
                    DismissableRoomTile(
                        room = room, modifier = Modifier
                            .height(56.dp)
                            .clickable { onClick() },
                        onDismiss = onDelete
                    )
                } else {
                    RoomTile(
                        room = room, modifier = Modifier
                            .height(56.dp)
                            .clickable { onClick() }
                    )
                }
                Divider()
            }
        }
    }