package com.example.hotel_application.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hotel_application.model.Data
import com.example.hotel_application.viewModel.MainViewModel
import com.example.hotel_application.R
import androidx.navigation.NavController


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Hotels List:")

        state.hotels.forEach { hotel ->
            ListingCard(hotel = hotel)
        }
    }
}

@Composable
fun ListingCard(hotel: Data) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        hotel.images.picture_url ?: "android.resource://${LocalContext.current.packageName}/${R.drawable.placeholder}"
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "${hotel.name} image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(hotel.name, style = MaterialTheme.typography.titleMedium)
                Text(hotel.price.toString(), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
