package com.example.promptforge.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "history") {
        composable("history") {
            HistoryScreen(
                onNew = { nav.navigate("improver") },
                onItem = { id -> nav.navigate("details/$id") }
            )
        }
        composable("improver") {
            ImproverScreen(onBack = { nav.popBackStack() })
        }
        composable(
            "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments!!.getInt("id")
            DetailsScreen(id = id, onBack = { nav.popBackStack() })
        }
    }
}
