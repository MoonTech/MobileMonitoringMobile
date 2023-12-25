package com.example.moontech.ui.screens.watch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moontech.data.dataclasses.Recording
import com.example.moontech.data.dataclasses.Recordings
import com.example.moontech.ui.components.CenterScreen

@Composable
fun RecordingsScreen(
    recordings: Recordings,
    onRecordingClicked: (Recording) -> Unit,
    modifier: Modifier = Modifier
) =
    CenterScreen(modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Reordings",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        ) {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(recordings.recordings, key = { it.url }) {
                    ElevatedCard(
                        modifier = modifier.padding(
                            top = 4.dp,
                            bottom = 4.dp,
                            start = 4.dp,
                            end = 4.dp
                        ).clickable { onRecordingClicked(it) }
                    ) {
                        Column(
                            modifier = modifier
                                .wrapContentSize()
                                .padding(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

    }