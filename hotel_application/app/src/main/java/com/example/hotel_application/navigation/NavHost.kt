package com.example.hotel_application.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.hotel_application.viewModel.MainViewModel

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

    Scaffold(
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

            composable("Details Screen/{_id}",
                arguments = listOf(navArgument("_id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("_id") ?: return@composable
                DetailsScreen(id = id)
            }
        }
    }
}
