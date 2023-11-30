package com.example.moontech.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.base.RoomListBase
import com.example.moontech.ui.screens.common.RoomType

@Composable
fun RoomListWithTabs(
    myRooms: List<ObjectWithRoomCode>,
    externalRooms: List<ObjectWithRoomCode>,
    onClick: (room: ObjectWithRoomCode) -> Unit,
    modifier: Modifier = Modifier,
    onTabClicked: (roomType: RoomType) -> Unit,
    tabs: List<RoomType>
) =
    CenterColumn {
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
        TabRow(selectedTabIndex = selectedTabIndex) {
            for (tab in tabs) {
                Tab(
                    selected = selectedTabIndex == tab.index,
                    onClick = { onTabClicked(tab) },
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(text = stringResource(tab.label))
                }
            }
        }
        RoomListBase(rooms = myRooms) {
            RoomCard(room = it) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Watch")
                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Transmit")
                    }
                }
            }
        }
    }

@Composable
@Preview
fun RoomListPreview() {
    val rooms = listOf(object : ObjectWithRoomCode {
        override val code: String = "123"
    })
    RoomListWithTabs(
        myRooms = rooms,
        externalRooms = rooms,
        onClick = { /*TODO*/ },
        onTabClicked = {},
        tabs = RoomType.values().asList())
}