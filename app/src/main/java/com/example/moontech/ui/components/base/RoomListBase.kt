package com.example.moontech.ui.components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.CenterColumn

@Composable
fun <T: ObjectWithRoomCode> RoomListBase(
    rooms: List<T>,
    modifier: Modifier = Modifier,
    roomCard: @Composable (room: T) -> Unit
) =
    CenterColumn(modifier = modifier, verticalArrangement = Arrangement.Top) {
        val listState = remember { mutableStateListOf<T>() }
        listState.clear()
        listState.addAll(rooms)
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = listState,
                key = { it.code },
            ) {
                val room by rememberUpdatedState(it)
                roomCard(room)
            }
        }
    }