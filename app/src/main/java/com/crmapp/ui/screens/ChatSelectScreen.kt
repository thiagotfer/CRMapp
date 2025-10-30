package com.crmapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crmapp.data.models.Client
import com.crmapp.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSelectionScreen(
    viewModel: AppViewModel,
    onNavigateToChat: (targetId: String, isGroup: Boolean) -> Unit
) {
    val chatPreviews = viewModel.messages
        .groupBy { it.chatId }
        .mapNotNull { (chatId, messages) ->
            val client = viewModel.clients.find { it.id == chatId }
            val lastMessage = messages.maxByOrNull { it.timestamp }

            if (client != null && lastMessage != null) {
                ChatPreview(
                    client = client,
                    lastMessage = lastMessage.content,
                    timestamp = lastMessage.timestamp,
                    unreadCount = 0
                )
            } else {
                null
            }
        }
        .sortedByDescending { it.timestamp }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Conversas", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(chatPreviews, key = { it.client.id }) { preview ->
                ChatPreviewCard(
                    preview = preview,
                    onClick = {
                        onNavigateToChat(preview.client.id, false)
                    }
                )
            }
        }
    }
}

private data class ChatPreview(
    val client: Client,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int
)

@Composable
private fun ChatPreviewCard(preview: ChatPreview, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarCircle(initial = preview.client.name.first())
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(preview.client.name, fontWeight = FontWeight.Bold)
            Text(
                text = preview.lastMessage,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(formatTimestampToTime(preview.timestamp), fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            if (preview.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = preview.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AvatarCircle(initial: Char) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial.toString(),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatTimestampToTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}