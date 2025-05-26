package com.example.hotel_application.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotel_application.model.Review

@Composable
fun ReviewList(
    reviews: List<Review>,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            reviews.take(3).forEach { review ->
                ReviewCard(
                    review = review,
                    modifier = Modifier
                        .width(280.dp)
                        .padding(end = 8.dp)
                )
            }
        }
        
        if (reviews.size > 3) {
            TextButton(
                onClick = onSeeMoreClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, top = 8.dp)
            ) {
                Text(
                    text = "See More Reviews (${reviews.size})",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
} 