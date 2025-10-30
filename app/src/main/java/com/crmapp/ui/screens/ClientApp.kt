

package com.crmapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.crmapp.viewmodel.AppViewModel

@Composable
fun ClientApp(
    viewModel: AppViewModel,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val currentUser = viewModel.currentUser!!

    Scaffold(
        bottomBar = {
            ClientBottomNavigationBar(navController = bottomNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = "Início",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Início") {
                ClientHomeScreen(
                    user = currentUser,
                    onNavigate = { route ->
                        // ✅ LÓGICA DE NAVEGAÇÃO CORRIGIDA
                        // Agora os botões usam a mesma lógica da barra de navegação,
                        // mantendo a pilha de telas sempre limpa.
                        bottomNavController.navigate(route) {
                            popUpTo(bottomNavController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable("Chat") {
                ChatScreen(
                    targetId = currentUser.id,
                    isGroup = false,
                    onBack = null,
                    viewModel = viewModel,
                    isOperatorView = false
                )
            }
            composable("Campanhas") {
                ClientCampaignScreen(
                    viewModel = viewModel,
                    navController = bottomNavController
                )
            }
            composable("Perfil") {
                ClientProfileScreen(
                    user = currentUser,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
private fun ClientBottomNavigationBar(navController: NavController) {
    val items = listOf(
        "Início" to Icons.Filled.Home,
        "Chat" to Icons.Filled.Chat,
        "Campanhas" to Icons.Filled.Campaign,
        "Perfil" to Icons.Filled.Person
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { (section, icon) ->
            NavigationBarItem(
                selected = currentRoute == section,
                onClick = {
                    if (currentRoute != section) {
                        navController.navigate(section) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(icon, contentDescription = section) },
                label = { Text(section) }
            )
        }
    }
}

