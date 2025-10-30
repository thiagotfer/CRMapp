package com.crmapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.crmapp.data.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(
    user: User,
    onNavigate: (route: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bem-vindo(a)!", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Olá, ${user.name}!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "O que você gostaria de fazer hoje?",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { onNavigate("Chat") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Falar com Atendimento")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onNavigate("Campanhas") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Minhas Promoções")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onNavigate("Perfil") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Ver Meus Cupons")
            }
        }
    }
}