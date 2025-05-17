package com.example.smartphone_app.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
}

@Composable
fun AuthNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier // Accept modifier here
) {
    NavHost(navController = navController, startDestination = Screen.Login.route, modifier = modifier) {
        composable(Screen.Login.route) {
            LoginScreen(onSignUpClick = {
                navController.navigate(Screen.SignUp.route)
            })
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(onLoginClick = {
                navController.navigate(Screen.Login.route)
            })
        }
    }
}