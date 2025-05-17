package com.example.smartphone_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.smartphone_app.ui.screens.AuthNavGraph
import com.example.smartphone_application.ui.theme.Smartphone_applicationTheme
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Smartphone_applicationTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding) // Apply padding here
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthNavGraph() {
    Smartphone_applicationTheme {
        val navController = rememberNavController()
        AuthNavGraph(navController = navController)
    }
}
