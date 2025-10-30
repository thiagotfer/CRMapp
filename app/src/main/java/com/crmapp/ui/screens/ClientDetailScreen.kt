package com.crmapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.crmapp.data.models.Client
import com.crmapp.data.models.Interaction
import com.crmapp.viewmodel.AppViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    client: Client,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onNavigateToChat: (targetId: String, isGroup: Boolean) -> Unit
) {
    var newNoteText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(client.name, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToChat(client.id, false) },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(Icons.Filled.Chat, contentDescription = "Enviar Mensagem", tint = Color.White)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Detalhes Rápidos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(8.dp))
                        DetailRow("Status:", client.status)
                        DetailRow("Score:", client.score.toString())
                        DetailRow("Tags:", client.tags.joinToString(", "))
                        DetailRow("Último Contato:", client.lastContact)
                    }
                }
            }

            item {
                Text("Adicionar Anotação", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                OutlinedTextField(
                    value = newNoteText,
                    onValueChange = { newNoteText = it },
                    label = { Text("Digite sua anotação...") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.addInteraction(client.id, "Anotação", newNoteText)
                        viewModel.showSnackbar("Anotação salva com sucesso!")
                        newNoteText = ""
                    },
                    enabled = newNoteText.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar Anotação")
                }
            }

            item {
                Text("Histórico de Interações", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            }
            items(
                items = client.interactions.sortedByDescending { it.timestamp },
                key = { it.id }
            ) { interaction ->
                InteractionCard(interaction)
            }
        }
    }
}

@Composable
private fun InteractionCard(interaction: Interaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = when (interaction.type) {
                    "Anotação" -> Icons.Default.Note
                    "Chat" -> Icons.Default.ChatBubble
                    "Campanha" -> Icons.Default.Campaign
                    else -> Icons.Default.History
                },
                contentDescription = interaction.type,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(interaction.content, style = MaterialTheme.typography.bodyMedium)
                Text(
                    formatFullTimestamp(interaction.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(120.dp))
        Text(value)
    }
}

fun formatFullTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}