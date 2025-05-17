package com.example.smartphone_application.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier

sealed class Pages(val route: String) {
    data object Login : Pages("login")
    data object SignUp : Pages("signup")
}

@Composable
fun AuthNavGraph(
    modifier: Modifier = Modifier, // Accept modifier here
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Pages.Login.route, modifier = modifier) {
        composable(Pages.Login.route) {
            LoginScreen(onSignUpClick = {
                navController.navigate(Pages.SignUp.route)
            })
        }
        composable(Pages.SignUp.route) {
            SignUpScreen(onLoginClick = {
                navController.navigate(Pages.Login.route)
            })
        }
    }
}