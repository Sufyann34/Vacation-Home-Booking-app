package com.example.hotel_application.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login Screen") {
        composable ("Login Screen") {
            AuthScreen(navController = navController)
        }
        composable ("Home Screen") {
            HomeScreen(navController = navController)
        }
    }
}