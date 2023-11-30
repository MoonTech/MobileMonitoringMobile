package com.example.moontech.ui.screens.base

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.FloatingActionButtonWithIcon
import com.example.moontech.ui.components.base.RoomListBase
import com.example.moontech.ui.screens.common.RoomType

@Composable
fun ListScreenBase(
    modifier: Modifier = Modifier,
    onRoomClick: (room: ObjectWithRoomCode) -> Unit,
    addRoom: () -> Unit = {},
    showTabs: Boolean = false,
    onDelete: ((room: ObjectWithRoomCode) -> Unit)? = null,
    onTabClicked: (roomType: RoomType) -> Unit = {},
    tabs: List<RoomType> = listOf(),
    rooms: Map<RoomType, List<ObjectWithRoomCode>>,
    selectedTab: RoomType = RoomType.MY_ROOMS,
    floatingActionButtonVisible: Boolean = false
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTabs) {
                TabRow(selectedTabIndex = selectedTab.index) {
                    for (tab in tabs) {
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { onTabClicked(tab) },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(text = stringResource(tab.label))
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButtonWithIcon(
                visible = floatingActionButtonVisible,
                onClick = addRoom,
                icon = Icons.Filled.Add,
                contentDescription = "Add room"
            )
        }
    ) {
        CenterScreen(modifier = Modifier.padding(it)) {
            RoomListBase(
                rooms = rooms.getOrElse(selectedTab) { listOf() },
                onClick = onRoomClick,
                modifier = Modifier.fillMaxSize(),
                onDelete = onDelete
            )
        }
    }
}