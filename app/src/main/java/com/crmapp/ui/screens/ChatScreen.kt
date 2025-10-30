package com.crmapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crmapp.data.models.Message
import com.crmapp.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private val quickCommands = mapOf(
    "/ola" to "Olá {{name}}, tudo bem? No que posso ajudar hoje?",
    "/agradecer" to "Obrigado pelo seu contato e preferência, {{name}}!",
    "/promo" to "Olá {{name}}, temos uma nova promoção especial para você! Gostaria de saber mais?",
    "/boleto" to "Claro, {{name}}, estou gerando a segunda via do seu boleto e enviarei em instantes."
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    targetId: String,
    isGroup: Boolean,
    onBack: (() -> Unit)?,
    viewModel: AppViewModel,
    isOperatorView: Boolean
) {
    val client = remember { viewModel.clients.find { it.id == targetId } }
    var newMessageText by remember { mutableStateOf("") }
    val chatMessages = viewModel.messages.filter { it.chatId == targetId }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showQuickCommands by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFECE5DD))) {
        TopAppBar(
            title = {
                if (isOperatorView && client != null) {
                    Column {
                        Text(client.name, fontSize = 18.sp)
                        Text(client.status, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                } else {
                    Text("Atendimento ao Cliente", fontSize = 18.sp)
                }
            },
            navigationIcon = {
                if (onBack != null) {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White) }
                }
            },
            actions = {
                IconButton(onClick = {}) { Icon(Icons.Default.Call, "Ligar", tint = Color.White) }
                IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, "Mais", tint = Color.White) }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            reverseLayout = true,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(chatMessages.reversed(), key = { it.id }) { message ->
                val isMe = message.senderId == viewModel.currentUser!!.id
                MessageBubble(message = message, isMe = isMe)
            }
        }

        if (showQuickCommands) {
            QuickCommandsPopup(
                clientName = client?.name ?: "cliente",
                onCommandSelected = { template ->
                    newMessageText = template
                    showQuickCommands = false
                }
            )
        }

        InputBar(
            value = newMessageText,
            onValueChange = { newText ->
                newMessageText = newText
                if (isOperatorView) {
                    showQuickCommands = (newText.trim() == "/")
                }
            },
            onSendClick = {
                val textToSend = newMessageText.trim()
                val template = if (isOperatorView) quickCommands[textToSend] else null
                val finalMessage = if (template != null && client != null) {
                    template.replace("{{name}}", client.name)
                } else {
                    textToSend
                }
                viewModel.sendMessage(finalMessage, targetId, isGroup)
                newMessageText = ""
                showQuickCommands = false
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        )
    }
}

@Composable
private fun QuickCommandsPopup(
    clientName: String,
    onCommandSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            quickCommands.forEach { (command, template) ->
                Text(
                    text = command,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val personalizedTemplate = template.replace("{{name}}", clientName)
                            onCommandSelected(personalizedTemplate)
                        }
                        .padding(16.dp)
                )
                if (command != quickCommands.keys.last()) {
                    Divider(color = Color.LightGray.copy(alpha = 0.2f))
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message, isMe: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 0.dp, bottomEnd = if (isMe) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = if (isMe) Color(0xFFDCF8C6) else Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(text = message.content, fontSize = 15.sp)
                Text(
                    text = formatTimestamp(message.timestamp),
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun InputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)) {
                IconButton(onClick = {}) { Icon(Icons.Default.AttachFile, "Anexar", tint = Color.Gray) }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f).padding(vertical = 12.dp),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) { Text("Digite uma mensagem", color = Color.Gray) }
                        innerTextField()
                    }
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = onSendClick,
            enabled = value.isNotBlank(),
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Icon(Icons.Filled.Send, contentDescription = "Enviar", tint = Color.White)
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}