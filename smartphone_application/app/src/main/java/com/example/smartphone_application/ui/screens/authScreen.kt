package com.example.smartphone_application.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onSignUpClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(label = { Text("Username") }, value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            label = { Text("Password") },
            value = "",
            onValueChange = {},
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) { Text("Login") }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onSignUpClick) { Text("Don't have an account? Sign Up") }
    }
}

@Composable
fun SignUpScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(label = { Text("Username") }, value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(label = { Text("Email") }, value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            label = { Text("Password") },
            value = "",
            onValueChange = {},
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) { Text("Sign Up") }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onLoginClick) { Text("Already have an account? Login") }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onSignUpClick = {})
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(onLoginClick = {})
}