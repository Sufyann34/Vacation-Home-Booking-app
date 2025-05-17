package com.example.smartphone_application.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.smartphone_application.api.listingData
import com.example.smartphone_application.model.listingModel
import com.example.smartphone_application.R

var limit = 10

@Composable
fun Listing(hotels: List<listingModel>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Available Stays", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(hotels.take(limit)) { hotel ->
                ListingCard(hotel)
            }
        }
    }
}

@Composable
fun ListingCard(hotel: listingModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        hotel.imageUrl ?: "android.resource://${LocalContext.current.packageName}/${R.drawable.placeholder}"
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
                Text(hotel.location, style = MaterialTheme.typography.bodyMedium)
                Text(hotel.pricePerNight, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListingPreview() {
    Listing(hotels = listingData)
}
