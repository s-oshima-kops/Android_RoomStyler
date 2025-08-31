package com.example.roomstyler.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roomstyler.ui.screen.catalog.CatalogScreen
import com.example.roomstyler.ui.screen.chat.ChatScreen
import com.example.roomstyler.ui.screen.home.HomeScreen
import com.example.roomstyler.ui.screen.preview.PreviewScreen
import com.example.roomstyler.ui.screen.proposals.ProposalsScreen
import com.example.roomstyler.ui.screen.scan.ScanScreen

@Composable
fun RoomStylerNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToScan = {
                    navController.navigate(Screen.Scan.route)
                },
                onNavigateToCatalog = {
                    navController.navigate(Screen.Catalog.route)
                },
                onNavigateToLayout = { layoutId ->
                    navController.navigate(Screen.Preview.createRoute(layoutId))
                }
            )
        }

        composable(Screen.Scan.route) {
            ScanScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToProposals = {
                    navController.navigate(Screen.Proposals.route)
                }
            )
        }

        composable(Screen.Proposals.route) {
            ProposalsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPreview = { layoutId ->
                    navController.navigate(Screen.Preview.createRoute(layoutId))
                }
            )
        }

        composable(
            route = Screen.Preview.route,
            arguments = Screen.Preview.arguments
        ) { backStackEntry ->
            val layoutId = backStackEntry.arguments?.getString("layoutId") ?: ""
            PreviewScreen(
                layoutId = layoutId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Catalog.route) {
            CatalogScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
