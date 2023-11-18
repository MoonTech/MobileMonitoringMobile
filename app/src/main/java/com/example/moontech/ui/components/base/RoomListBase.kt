package com.example.moontech.ui.components.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontech.ui.components.CenterColumn
import com.example.moontech.ui.components.RoomTile
import com.example.moontech.ui.viewmodel.dataclasses.Room

@Composable
fun RoomListBase(rooms: List<Room>, onClick: () -> Unit, modifier: Modifier = Modifier) =
    CenterColumn(modifier = modifier, verticalArrangement = Arrangement.Top) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(
                items = rooms,
                key = { it.code },
                contentType = { Room::class.java }
            ) { room ->
                RoomTile(room = room, modifier = Modifier
                    .height(56.dp)
                    .clickable { onClick() })
                HorizontalDivider()
            }
        }
    }