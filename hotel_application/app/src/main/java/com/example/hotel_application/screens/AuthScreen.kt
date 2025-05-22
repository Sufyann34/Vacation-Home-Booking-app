package com.example.hotel_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.hotel_application.viewModel.AuthViewModel

@Composable
fun AuthScreen(navController: NavHostController, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }

    val authViewModel: AuthViewModel = viewModel()
    val loginResponse by authViewModel.loginResponse.collectAsState()
    val signupResponse by authViewModel.signupResponse.collectAsState()

    val isFormValid = if (isLoginMode) {
        username.isNotBlank() && password.isNotBlank()
    } else {
        username.isNotBlank() && email.isNotBlank() && password.isNotBlank()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isLoginMode) "Login" else "Sign Up",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username*") },
                modifier = Modifier.fillMaxWidth()
            )

            if (!isLoginMode) {
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email*") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password*") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isLoginMode) {
                        authViewModel.login(username, password)
                    } else {
                        authViewModel.signup(username, password, email)
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoginMode) "Login" else "Sign Up")
            }

            if (!isFormValid) {
                Text(
                    "Please fill out all required fields",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { isLoginMode = !isLoginMode }) {
                Text(
                    if (isLoginMode) "Don't have an account? Sign Up"
                    else "Already have an account? Login"
                )
            }
            // Optional: Display API response
            loginResponse?.let {
                if (it.isSuccessful) {
                    Text("Success! Token: ${it.body()?.token}")
                    navController?.navigate("Home") // <-- navigate to MainScreen instead
                    onLoginSuccess()
                } else {
                    val errorMessage = it.errorBody()?.string() ?: "Unknown error"
                    Text("Authentication Failed $errorMessage", color = MaterialTheme.colorScheme.error)
                }
            }

            signupResponse?.let {
                if (it.isSuccessful) {
                    Text("Signup Successful")
                    navController?.navigate("Login")
//                    onLoginSuccess()
                } else {
                    val errorMessage = it.errorBody()?.string() ?: "Unknown error"
                    Text("Signup Failed: $errorMessage", color = MaterialTheme.colorScheme.error)
                }
            }

        }
    }
}