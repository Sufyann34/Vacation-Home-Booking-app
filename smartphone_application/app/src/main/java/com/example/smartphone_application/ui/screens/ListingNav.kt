package com.example.smartphone_application.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.example.smartphone_application.api.listingData

sealed class Screen(val route: String) {
    data object Listing : Screen("listing")
}

@Composable
fun ListingNav(
    modifier: Modifier = Modifier, // Accept modifier here
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Listing.route, modifier = modifier) {
        composable(Screen.Listing.route) {
            Listing(hotels = listingData)
        }
    }
}