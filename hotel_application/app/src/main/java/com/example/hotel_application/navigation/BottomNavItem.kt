package com.example.hotel_application.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object About : BottomNavItem("about", "About", Icons.Filled.Info)
    object Logout : BottomNavItem("logout", "Logout", Icons.Filled.PowerSettingsNew)

    companion object {
        fun items() = listOf(Home, About, Logout)

        fun fromRoute(route: String?): BottomNavItem? {
            Log.d("BottomNavItem", "Matching route: $route")  // Log the route being passed
            return when (route) {
                Home.route -> {
                    Log.d("BottomNavItem", "Matched Home route")
                    Home
                }
                About.route -> {
                    Log.d("BottomNavItem", "Matched About route")
                    About
                }
                Logout.route -> {
                    Log.d("BottomNavItem", "Matched Logout route")
                    Logout
                }
                else -> {
                    Log.d("BottomNavItem", "Route not found: $route")
                    null
                }
            }
        }
    }

}

