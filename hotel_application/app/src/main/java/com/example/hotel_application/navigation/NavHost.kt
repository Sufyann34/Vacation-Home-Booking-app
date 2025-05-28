package com.example.hotel_application.navigation

import android.graphics.Color.red
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hotel_application.components.BottomNavigationBar
import com.example.hotel_application.screens.AuthScreen
import com.example.hotel_application.screens.DetailsScreen
import com.example.hotel_application.screens.HomeScreen
import com.example.hotel_application.screens.AboutScreen
import com.example.hotel_application.screens.DetailedReviewsScreen
import com.example.hotel_application.viewModel.MainViewModel
import com.example.hotel_application.navigation.Screen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = viewModel.isLoggedIn && currentRoute in listOf(
        BottomNavItem.Home.route,
        BottomNavItem.About.route,
        BottomNavItem.Logout.route
    )

    Scaffold (
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        },

        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (viewModel.isLoggedIn) BottomNavItem.Home.route else "Login"
        ) {
            composable("Login") {
                AuthScreen(
                    navController,
                    onLoginSuccess = {
                        viewModel.login()
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("Login") { inclusive = true }
                        }
                    }
                )
            }

            composable(BottomNavItem.Home.route) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {
                    HomeScreen(navController, viewModel)
                }
            }

            composable(BottomNavItem.About.route) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {
                    AboutScreen()
                }
            }

            composable(BottomNavItem.Logout.route) {
                viewModel.logout()
                navController.navigate("Login")
                {
                    popUpTo(BottomNavItem.Home.route) { inclusive = true }
                }
            }

            composable(Screen.Details.route + "/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                DetailsScreen(
                    id = id,
                    onNavigateToReviews = {
                        navController.navigate(Screen.DetailedReviews.route + "/$id")
                    }
                )
            }

            composable(Screen.DetailedReviews.route + "/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                DetailedReviewsScreen(
                    listingId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
