package com.buuyst07.cusa.ui

import android.media.projection.MediaProjectionManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buuyst07.cusa.*
import kotlin.math.roundToInt

@Composable
fun StreamingScreen(
    account: StreamAccount?,
    config: StreamConfig,
    mediaProjectionManager: MediaProjectionManager,
    onStopStream: () -> Unit,
    activity: ComponentActivity
) {
    var isStreaming by remember { mutableStateOf(false) }
    var streamStats by remember { mutableStateOf(StreamStats()) }
    var chatMessages by remember { mutableStateOf(emptyList<ChatMessage>()) }
    var chatVisible by remember { mutableStateOf(true) }
    var chatOffset by remember { mutableStateOf(IntOffset(0, 0)) }

    LaunchedEffect(Unit) {
        if (account != null && !isStreaming) {
            activity.startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                MainActivity.PROJECTION_REQUEST_CODE
            )
            isStreaming = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    ),
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                            Text(
                                text = "LIVE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Text(
                            text = account?.displayName ?: "Unknown",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${streamStats.viewerCount} viewers",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = formatDuration(streamStats.streamDuration),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StreamQualityBadge(
                        label = "Bitrate",
                        value = "${streamStats.bitrateCurrent} kbps"
                    )
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    StreamQualityBadge(
                        label = "FPS",
                        value = "${streamStats.fpsCurrent} fps"
                    )
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    StreamQualityBadge(
                        label = "Dropped",
                        value = "${streamStats.droppedFrames} frames"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Chat Overlay",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = chatVisible,
                    onCheckedChange = { chatVisible = it }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    isStreaming = false
                    onStopStream()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    "Stop Stream",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (chatVisible) {
            ChatOverlay(
                messages = chatMessages,
                offset = chatOffset,
                onOffsetChange = { chatOffset = it }
            )
        }
    }
}

@Composable
fun ChatOverlay(
    messages: List<ChatMessage>,
    offset: IntOffset,
    onOffsetChange: (IntOffset) -> Unit
) {
    Box(
        modifier = Modifier
            .offset { offset }
            .width(300.dp)
            .heightIn(max = 400.dp)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    onOffsetChange(
                        IntOffset(
                            (offset.x + dragAmount.x.roundToInt()).coerceIn(-1000, 1000),
                            (offset.y + dragAmount.y.roundToInt()).coerceIn(-1000, 1000)
                        )
                    )
                }
            }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            )
        ) {
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Waiting for chat...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    items(messages.takeLast(20)) { message ->
                        ChatMessageItem(message)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Column(modifier = Modifier.padding(4.dp)) {
        Row {
            Text(
                text = message.author,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = when (message.platform) {
                    Platform.YOUTUBE -> MaterialTheme.colorScheme.error
                    Platform.TWITCH -> MaterialTheme.colorScheme.primary
                }
            )
            Text(
                text = if (message.platform == Platform.YOUTUBE) " [YT]" else " [TV]",
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Text(
            text = message.text,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun StreamQualityBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun formatDuration(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return when {
        hours > 0 -> "$hours:${String.format("%02d", minutes % 60)}:${String.format("%02d", seconds % 60)}"
        minutes > 0 -> "$minutes:${String.format("%02d", seconds % 60)}"
        else -> "0:${String.format("%02d", seconds)}"
    }
}
