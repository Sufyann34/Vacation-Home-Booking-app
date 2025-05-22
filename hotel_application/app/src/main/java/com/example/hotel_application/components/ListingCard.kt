package com.example.hotel_application.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hotel_application.R
import com.example.hotel_application.model.Data

@Composable
fun ListingCard(hotel: Data, itemIndex: Int, hotelList: List<Data>, navController: NavHostController) {
//fun ListingCard() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
    ) {
        Column() {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
//                        "https://unsplash.com/photos/gray-wooden-house-178j8tJrNlc"
                        hotel.images.picture_url ?: "android.resource://${LocalContext.current.packageName}/${R.drawable.placeholder}"
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "${hotel.name} image",
//                contentDescription = "This is image description",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
        Text(
            text = "Elevated",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable {
//                navController.navigate("Details Screen/${hotelList[itemIndex]._id}")
//            },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column {
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(
//                        hotel.images.picture_url ?: "android.resource://${LocalContext.current.packageName}/${R.drawable.placeholder}"
//                    )
//                    .crossfade(true)
//                    .build(),
//                contentDescription = "${hotel.name} image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(hotel.name, style = MaterialTheme.typography.titleMedium)
//                Text(hotel.price.toString(), style = MaterialTheme.typography.bodyLarge)
//            }
//        }
//    }
}

//@Preview(showBackground = true)
//@Composable
//fun ListingCardPreview() {
//    ListingCard()
//}