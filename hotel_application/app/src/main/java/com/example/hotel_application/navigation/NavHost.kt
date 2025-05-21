package com.example.hotel_application.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login Screen") {
        composable ("Login Screen") {
            LoginScreen(navController = navController)
        }
        composable ("Home Screen") {
            HomeScreen(navController = navController)
        }
        composable ("Details Screen/{id}",
            arguments = listOf(
                navArgument(
                    name = "_id"
                ) {
                    type = NavType.IntType
                }
            )) { id ->
            id.arguments?.getInt("_id")?.let{ id1 ->
                DetailsScreen(id = id1)
            }
        }
    }
}