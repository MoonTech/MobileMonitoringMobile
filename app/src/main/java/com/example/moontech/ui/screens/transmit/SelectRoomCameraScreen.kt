package com.example.moontech.ui.screens.transmit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.RoomCard
import com.example.moontech.ui.screens.base.ListScreenBase
import com.example.moontech.ui.screens.common.RoomType

@Composable
fun <T : ObjectWithRoomCode> SelectRoomCameraScreen(
    rooms: Map<RoomType, List<T>>,
    addCamera: () -> Unit,
    selectCamera: (T) -> Unit,
    modifier: Modifier = Modifier,
    onSettings: (room: T) -> Unit,
) {
    var selectedTab by rememberSaveable { mutableStateOf(RoomType.EXTERNAL) }
    ListScreenBase(
        modifier = modifier,
        onRoomClick = { selectCamera(it) },
        addRoom = addCamera,
        rooms = rooms[selectedTab] ?: listOf(),
        floatingActionButtonVisible = selectedTab.index == RoomType.EXTERNAL.index,
        topBar = {
            TabRow(selectedTabIndex = selectedTab.index) {
                for (roomType in rooms.keys) {
                    Tab(
                        selected = selectedTab.index == roomType.index,
                        onClick = { selectedTab = roomType },
                        text = { Text(text = stringResource(roomType.label)) })
                }
            }
        }
    ) { room ->
        RoomCard(room = room, rowTitleContent = {
            IconButton(onClick = {
                onSettings(room)
            }) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Setting")
            }
        }) { }
    }
}