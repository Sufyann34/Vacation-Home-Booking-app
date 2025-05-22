package com.example.hotel_application.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hotel_application.model.Data
import com.example.hotel_application.viewModel.MainViewModel
import com.example.hotel_application.R
//import com.example.hotel_application.components.ListingCard

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val state = viewModel.state
    val currentPage = state.page
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Title and page info
        Text(
            text = "Hotels",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Page $currentPage",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search hotels...") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hotel list
        state.hotels.forEachIndexed { index, hotel ->
            ListingCard(
                hotel = hotel,
                itemIndex = index,
                hotelList = state.hotels,
                navController = navController as NavHostController
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Pagination buttons
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.previousPage() },
                enabled = currentPage > 1
            ) {
                Text("Previous")
            }

            Button(
                onClick = { viewModel.nextPage() },
                enabled = state.hotels.isNotEmpty()
            ) {
                Text("Next")
            }
        }
    }
}


@Composable
fun ListingCard(
    hotel: Data,
    itemIndex: Int,
    hotelList: List<Data>,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("Details Screen/${hotelList[itemIndex]._id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        hotel.images.picture_url
                            ?: "android.resource://${LocalContext.current.packageName}/${R.drawable.placeholder}"
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
                Text(
                    text = "Price: ${hotel.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

