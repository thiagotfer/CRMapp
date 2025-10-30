package com.crmapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.crmapp.viewmodel.AppViewModel

object AppRoutes {
    const val LOGIN = "login"
    const val OPERATOR_HOME = "operator_home"
    const val CLIENT_HOME = "client_home"
    const val CHAT = "chat/{targetId}/{isGroup}"
    const val CLIENT_DETAIL = "client_detail/{clientId}"
    fun chatRoute(targetId: String, isGroup: Boolean) = "chat/$targetId/$isGroup"
    fun clientDetailRoute(clientId: String) = "client_detail/$clientId"
}

@Composable
fun AppNavigation(viewModel: AppViewModel = viewModel()) {
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = AppRoutes.LOGIN) {
            composable(AppRoutes.LOGIN) {
                LoginScreen(onLogin = { isOperator ->
                    viewModel.login(isOperator)
                    val destination = if (isOperator) AppRoutes.OPERATOR_HOME else AppRoutes.CLIENT_HOME
                    navController.navigate(destination) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                })
            }
            composable(AppRoutes.OPERATOR_HOME) {
                OperatorApp(
                    viewModel = viewModel,
                    onNavigateToDetail = { clientId -> navController.navigate(AppRoutes.clientDetailRoute(clientId)) },
                    onNavigateToChat = { targetId, isGroup -> navController.navigate(AppRoutes.chatRoute(targetId, isGroup)) },
                    onLogout = {
                        viewModel.logout()
                        navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } }
                    }
                )
            }
            composable(AppRoutes.CLIENT_HOME) {
                ClientApp(
                    viewModel = viewModel,
                    onLogout = {
                        viewModel.logout()
                        navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.CLIENT_HOME) { inclusive = true } }
                    }
                )
            }
            composable(
                route = AppRoutes.CHAT,
                arguments = listOf(
                    navArgument("targetId") { type = NavType.StringType },
                    navArgument("isGroup") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val targetId = backStackEntry.arguments?.getString("targetId") ?: ""
                val isGroup = backStackEntry.arguments?.getBoolean("isGroup") ?: false
                ChatScreen(
                    targetId = targetId,
                    isGroup = isGroup,
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel,
                    isOperatorView = true
                )
            }
            composable(
                route = AppRoutes.CLIENT_DETAIL,
                arguments = listOf(navArgument("clientId") { type = NavType.StringType })
            ) { backStackEntry ->
                val clientId = backStackEntry.arguments?.getString("clientId")
                val client = viewModel.clients.find { it.id == clientId }
                if (client != null) {
                    ClientDetailScreen(
                        client = client,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNavigateToChat = { targetId, isGroup -> navController.navigate(AppRoutes.chatRoute(targetId, isGroup)) }
                    )
                }
            }
        }
    }
    NavHost(navController = navController, graph = navGraph)
}