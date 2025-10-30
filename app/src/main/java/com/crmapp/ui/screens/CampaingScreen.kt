package com.crmapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crmapp.viewmodel.AppViewModel
import com.crmapp.viewmodel.CampaignChannel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignScreen(viewModel: AppViewModel) {
    var selectedStatus by remember { mutableStateOf("Todos") }
    var scoreRange by remember { mutableStateOf(1f..5f) }
    var messageContent by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }
    var selectedChannel by remember { mutableStateOf(CampaignChannel.CAMPAIGN) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    val filteredClients = viewModel.clients.filter { client ->
        val statusMatch = selectedStatus == "Todos" || client.status == selectedStatus
        val scoreMatch = client.score in scoreRange.start.roundToInt()..scoreRange.endInclusive.roundToInt()
        statusMatch && scoreMatch
    }
    val statusOptions = listOf("Todos") + viewModel.clients.map { it.status }.distinct()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Campanhas Direcionadas", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("1. Defina o Público-Alvo", style = MaterialTheme.typography.titleLarge)
            }
            item {
                FilterDropdown(
                    options = statusOptions,
                    selectedOption = selectedStatus,
                    onOptionSelected = { selectedStatus = it }
                )
            }
            item {
                Column {
                    Text(
                        "Filtrar por Score: (${scoreRange.start.roundToInt()} - ${scoreRange.endInclusive.roundToInt()})",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    RangeSlider(value = scoreRange, onValueChange = { scoreRange = it }, valueRange = 1f..5f, steps = 3)
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(text = "Destinatários: ${filteredClients.size} clientes", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                }
            }
            item {
                Text("2. Escolha o Canal de Envio", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedChannel == CampaignChannel.CAMPAIGN, onClick = { selectedChannel = CampaignChannel.CAMPAIGN })
                    Text("Campanha (Aba 'Novidades' do cliente)", modifier = Modifier.clickable { selectedChannel = CampaignChannel.CAMPAIGN })
                }
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedChannel == CampaignChannel.CHAT, onClick = { selectedChannel = CampaignChannel.CHAT })
                    Text("Chat (Mensagem direta na conversa)", modifier = Modifier.clickable { selectedChannel = CampaignChannel.CHAT })
                }
            }
            item {
                Text("3. Escreva a Mensagem", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp))
                OutlinedTextField(
                    value = messageContent,
                    onValueChange = { messageContent = it },
                    label = { Text("Promoção, aviso ou comunicado...") },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    maxLines = 5
                )
            }
            item {
                Button(
                    onClick = { showConfirmation = true },
                    enabled = messageContent.isNotBlank() && filteredClients.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Revisar Envio para ${filteredClients.size} clientes")
                }
            }
        }

        if (showConfirmation) {
            AlertDialog(
                onDismissRequest = { showConfirmation = false },
                title = { Text("Confirmar Envio") },
                text = { Text("Você está prestes a enviar esta mensagem para ${filteredClients.size} clientes através do canal '${selectedChannel.name.lowercase()}'. Deseja continuar?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.sendMassMessage(filteredClients, messageContent, selectedChannel)
                        viewModel.showSnackbar("Mensagem enviada para ${filteredClients.size} clientes!")
                        showConfirmation = false
                        messageContent = ""
                    }) {
                        Text("Sim, Enviar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmation = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}