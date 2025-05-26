package com.example.hotel_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hotel_application.navigation.Navigation
import com.example.hotel_application.ui.theme.Hotel_applicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hotel_applicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val mainViewModel = viewModel<MainViewModel>()
//                    val state = mainViewModel.state

                    Column(modifier = Modifier) {
                        Navigation()
                    }
                }
            }
        }
    }
}
