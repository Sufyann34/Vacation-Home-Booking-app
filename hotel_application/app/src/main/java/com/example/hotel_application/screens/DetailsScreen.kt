package com.example.hotel_application.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hotel_application.viewModel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.log

@Composable
fun DetailsScreen (id: String){
    val mainViewModel = viewModel<MainViewModel>()
    mainViewModel.id = id
    mainViewModel.getDetailsById(id)
    val state = mainViewModel.state
    val details = state.detailsData

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.Black)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Hotel Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(details?.images?.picture_url ?: "")
                .crossfade(true)
                .build(),
            contentDescription = "Hotel Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        details?.let {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Name: ${it.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Price per night: €${it.price}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Property Type: ${it.property_type}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Address: ${it.address?.street ?: "(Street not provided)"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Text(
                    text = it.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

    }
}