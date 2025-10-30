package com.crmapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crmapp.data.models.Client
import com.crmapp.viewmodel.AppViewModel
import kotlin.math.roundToInt

private enum class SortOption { Score, Name }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    viewModel: AppViewModel,
    onNavigateToDetail: (clientId: String) -> Unit,
    onNavigateToChat: (targetId: String, isGroup: Boolean) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("Todos") }
    var scoreRange by remember { mutableStateOf(1f..5f) }
    var sortOption by remember { mutableStateOf(SortOption.Score) }
    val selectedTags = remember { mutableStateListOf<String>() }

    val statusOptions = listOf("Todos") + viewModel.clients.map { it.status }.distinct()
    val allTags = remember { viewModel.clients.flatMap { it.tags }.distinct() }

    val filteredAndSortedClients = remember(searchText, selectedStatus, scoreRange, sortOption, selectedTags.toList(), viewModel.clients) {
        val filtered = viewModel.clients.filter { client ->
            val nameMatch = client.name.contains(searchText, ignoreCase = true)
            val statusMatch = selectedStatus == "Todos" || client.status == selectedStatus
            val scoreMatch = client.score in scoreRange.start.roundToInt()..scoreRange.endInclusive.roundToInt()
            val tagsMatch = selectedTags.isEmpty() || client.tags.any { it in selectedTags }
            nameMatch && statusMatch && scoreMatch && tagsMatch
        }

        when (sortOption) {
            SortOption.Score -> filtered.sortedByDescending { it.score }
            SortOption.Name -> filtered.sortedBy { it.name }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Clientes CRM", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Buscar por nome...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterDropdown(
                    options = statusOptions,
                    selectedOption = selectedStatus,
                    onOptionSelected = { selectedStatus = it },
                    modifier = Modifier.weight(1f)
                )
                SortOptionsRow(
                    selectedOption = sortOption,
                    options = SortOption.values().toList(),
                    onOptionSelected = { sortOption = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                "Score: (${scoreRange.start.roundToInt()} - ${scoreRange.endInclusive.roundToInt()})",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            RangeSlider(
                value = scoreRange,
                onValueChange = { scoreRange = it },
                valueRange = 1f..5f,
                steps = 3
            )

            Text("Tags:", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(allTags) { tag ->
                    FilterChip(
                        selected = tag in selectedTags,
                        onClick = {
                            if (tag in selectedTags) selectedTags.remove(tag)
                            else selectedTags.add(tag)
                        },
                        label = { Text(tag) }
                    )
                }
            }
        }

        LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
            items(filteredAndSortedClients, key = { it.id }) { client ->
                ClientListItem(
                    client = client,
                    onClientClick = { onNavigateToDetail(client.id) },
                    onChatClick = { onNavigateToChat(client.id, false) }
                )
                Divider()
            }
        }
    }
}

@Composable
fun ClientListItem(
    client: Client,
    onClientClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClientClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val scoreColor = when (client.score) {
                5, 4 -> Color(0xFF4CAF50)
                3 -> Color(0xFFFFC107)
                else -> Color(0xFFF44336)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(scoreColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = client.score.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(client.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(client.status, color = Color.Gray, fontSize = 12.sp)
            }
            IconButton(onClick = onChatClick) {
                Icon(
                    Icons.Default.ChatBubbleOutline,
                    contentDescription = "Abrir Chat",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            textStyle = MaterialTheme.typography.bodySmall
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SortOptionsRow(
    selectedOption: T,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
            ) {
                Text(option.toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}