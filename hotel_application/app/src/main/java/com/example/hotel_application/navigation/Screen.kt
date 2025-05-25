package com.example.hotel_application.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object About : Screen("about")
    object Details : Screen("details")
    object DetailedReviews : Screen("detailed_reviews")
    object Login : Screen("login")
} 