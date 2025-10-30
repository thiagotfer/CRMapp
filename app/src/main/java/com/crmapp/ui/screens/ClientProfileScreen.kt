package com.crmapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crmapp.data.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileScreen(user: User, onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Meu Perfil", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text("Cliente", color = Color.Gray, fontSize = 16.sp)
                Spacer(Modifier.height(32.dp))
            }

            if (user.coupons.isNotEmpty()) {
                item {
                    Text(
                        text = "Meus Cupons",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
                items(user.coupons, key = { it.id }) { coupon ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        ListItem(
                            headlineContent = { Text(coupon.code, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text(coupon.description) },
                            overlineContent = { Text("Válido até: ${coupon.expiryDate}", style = MaterialTheme.typography.labelSmall) },
                            leadingContent = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "Cupom") }
                        )
                    }
                }
                item {
                    Spacer(Modifier.height(32.dp))
                }
            }

            item {
                Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
                    Text("Simular Logout")
                }
            }
        }
    }
}