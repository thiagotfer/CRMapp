package com.crmapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.crmapp.data.models.Interaction
import com.crmapp.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientCampaignScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val currentClientId = viewModel.currentUser?.id
    val realCampaigns = viewModel.clients
        .find { it.id == currentClientId }
        ?.interactions
        ?.filter { it.type == "Campanha" }
        ?: emptyList()

    val exampleCampaigns = listOf(
        Interaction(
            type = "Campanha",
            content = "Seus dados estão seguros. Visite [seu perfil](Perfil) para mais detalhes.",
            timestamp = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30)
        ),
        Interaction(
            type = "Campanha",
            content = "Só hoje! 20% de desconto em todo o site usando o cupom PROMO20.",
            timestamp = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)
        ),
        Interaction(
            type = "Campanha",
            content = "Frete grátis para compras acima de R$150,00. Aproveite!",
            timestamp = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        )
    )

    val campaignsToShow = if (realCampaigns.isEmpty()) {
        exampleCampaigns
    } else {
        realCampaigns
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Novidades e Campanhas", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(campaignsToShow.sortedByDescending { it.timestamp }, key = { it.id }) { campaign ->
                CampaignItemCard(
                    interaction = campaign,
                    onLinkClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CampaignItemCard(
    interaction: Interaction,
    onLinkClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Default.Campaign,
                contentDescription = "Campanha",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                LinkableText(
                    text = interaction.content,
                    onLinkClick = onLinkClick
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = formatFullTimestamp(interaction.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}