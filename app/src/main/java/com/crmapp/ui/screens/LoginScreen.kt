package com.crmapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(onLogin: (isOperator: Boolean) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isOperatorLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("CRM Mobile", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextButton(
                onClick = { isOperatorLogin = true },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (isOperatorLogin) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isOperatorLogin) Color.White else Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Operador")
            }
            TextButton(
                onClick = { isOperatorLogin = false },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (!isOperatorLogin) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (!isOperatorLogin) Color.White else Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cliente")
            }
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha (simulada)") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onLogin(isOperatorLogin) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Entrar", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}