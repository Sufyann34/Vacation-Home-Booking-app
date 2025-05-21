package com.example.hotel_application.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hotel_application.viewModel.AuthViewModel


@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = viewModel()
    val response by authViewModel.loginResponse.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                authViewModel.login(username, password)

                navController.navigate("Home Screen")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        response?.let {
            if (it.isSuccessful) {
                Text("Login Success! Token: ${it.body()?.token}")
                // Navigate to home
                // navController.navigate("home")
            } else {
                Text("Login Failed", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}