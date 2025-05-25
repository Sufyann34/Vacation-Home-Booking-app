package com.example.hotel_application.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hotel_application.viewModel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.hotel_application.model.Review
import com.example.hotel_application.model.ReviewScores
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(id: String) {
    val mainViewModel = viewModel<MainViewModel>()
    mainViewModel.id = id
    mainViewModel.getDetailsById(id)
    mainViewModel.getReviews(id)
    val state = mainViewModel.state
    val details = state.detailsData
    var showAddReviewDialog by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.Black)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        item {
            Text(
                text = "Hotel Details",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
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
        }

        details?.let { it ->
            item {
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

        // Review Scores Section
        details?.review_scores?.let { scores ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Review Scores",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ReviewScoreItem("Overall Rating", scores.review_scores_rating)
                        ReviewScoreItem("Accuracy", scores.review_scores_accuracy)
                        ReviewScoreItem("Cleanliness", scores.review_scores_cleanliness)
                        ReviewScoreItem("Check-in", scores.review_scores_checkin)
                        ReviewScoreItem("Communication", scores.review_scores_communication)
                        ReviewScoreItem("Location", scores.review_scores_location)
                        ReviewScoreItem("Value", scores.review_scores_value)
                    }
                }
            }
        }

        // Reviews Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    onClick = { showAddReviewDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Review")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Review")
                }
            }
        }

        items(state.reviews) { review ->
            ReviewCard(
                review = review,
                onDelete = { mainViewModel.deleteReview(id, review._id) }
            )
        }
    }

    if (showAddReviewDialog) {
        AddReviewDialog(
            onDismiss = { showAddReviewDialog = false },
            onSubmit = { review ->
                mainViewModel.addReview(id, review)
                showAddReviewDialog = false
            }
        )
    }
}

@Composable
fun ReviewScoreItem(label: String, score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = "$score/10")
    }
}

@Composable
fun ReviewCard(
    review: Review,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.reviewer_name,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Review")
                }
            }
            Text(
                text = review.comments,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            review.review_scores?.let { scores ->
                Text(
                    text = "Rating: ${scores.review_scores_rating}/10",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (Review) -> Unit
) {
    var comments by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    var accuracy by remember { mutableStateOf(5) }
    var cleanliness by remember { mutableStateOf(5) }
    var checkin by remember { mutableStateOf(5) }
    var communication by remember { mutableStateOf(5) }
    var location by remember { mutableStateOf(5) }
    var value by remember { mutableStateOf(5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Review") },
        text = {
            Column {
                OutlinedTextField(
                    value = comments,
                    onValueChange = { comments = it },
                    label = { Text("Comments") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Rating", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = rating.toFloat(),
                    onValueChange = { rating = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                Text("Accuracy", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = accuracy.toFloat(),
                    onValueChange = { accuracy = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                Text("Cleanliness", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = cleanliness.toFloat(),
                    onValueChange = { cleanliness = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                Text("Check-in", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = checkin.toFloat(),
                    onValueChange = { checkin = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                Text("Communication", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = communication.toFloat(),
                    onValueChange = { communication = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                Text("Location", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = location.toFloat(),
                    onValueChange = { location = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                Text("Value", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = value.toFloat(),
                    onValueChange = { value = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reviewScores = ReviewScores(
                        review_scores_accuracy = accuracy,
                        review_scores_cleanliness = cleanliness,
                        review_scores_checkin = checkin,
                        review_scores_communication = communication,
                        review_scores_location = location,
                        review_scores_value = value,
                        review_scores_rating = rating
                    )
                    val review = Review(
                        _id = UUID.randomUUID().toString(),
                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                        listing_id = "",
                        reviewer_id = "user123", // TODO: Get actual user ID
                        reviewer_name = "User", // TODO: Get actual user name
                        comments = comments,
                        review_scores = reviewScores
                    )
                    onSubmit(review)
                }
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}