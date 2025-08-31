package com.example.roomstyler.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Screen("home")
    
    object Scan : Screen("scan")
    
    object Proposals : Screen("proposals")
    
    object Preview : Screen(
        route = "preview/{layoutId}",
        arguments = listOf(
            navArgument("layoutId") {
                type = NavType.StringType
            }
        )
    ) {
        fun createRoute(layoutId: String) = "preview/$layoutId"
    }
    
    object Catalog : Screen("catalog")
    
    object Chat : Screen("chat")
}
