package com.crmapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.crmapp.viewmodel.AppViewModel

@Composable
fun OperatorApp(
    viewModel: AppViewModel,
    onNavigateToDetail: (clientId: String) -> Unit,
    onNavigateToChat: (targetId: String, isGroup: Boolean) -> Unit,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = "CRM",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("CRM") {
                ClientListScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToChat = onNavigateToChat
                )
            }
            composable("Chat") {
                ChatSelectionScreen(
                    viewModel = viewModel,
                    onNavigateToChat = onNavigateToChat
                )
            }
            composable("Campanha") {
                CampaignScreen(viewModel = viewModel)
            }
            composable("Perfil") {
                OperatorProfileScreen(
                    user = viewModel.currentUser!!,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        "CRM" to Icons.Filled.People,
        "Chat" to Icons.Filled.Message,
        "Campanha" to Icons.Filled.Send,
        "Perfil" to Icons.Filled.Person
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { (section, icon) ->
            NavigationBarItem(
                selected = currentRoute == section,
                onClick = {
                    navController.navigate(section) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = section) },
                label = { Text(section) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}