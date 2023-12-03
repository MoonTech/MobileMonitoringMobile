package com.example.moontech.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.ObjectWithRoomCode

@Composable
fun RoomCard(
    room: ObjectWithRoomCode,
    modifier: Modifier = Modifier,
    rowTitleContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    ElevatedCard(modifier = modifier.padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = room.code,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable { })
                Spacer(modifier = Modifier.weight(1f))
                rowTitleContent()
            }
            content()
        }
    }
}

@Preview
@Composable
fun RoomCardPreview() {
    val room = object : ObjectWithRoomCode {
        override val code: String = "My room 1"
    }
    RoomCard(room = room, rowTitleContent = {
        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Setting")
    }) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Watch", modifier = Modifier
                .padding(end = 8.dp)
                .clickable {

                })
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = "Transmit")
            }
        }
    }
}