package com.example.hotel_application.screens

import android.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hotel_application.viewModel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.hotel_application.model.Review
import com.example.hotel_application.model.ReviewScores
import com.example.hotel_application.components.ReviewList
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    id: String,
    onNavigateToReviews: () -> Unit
) {
    val mainViewModel = viewModel<MainViewModel>()
    mainViewModel.getDetailsById(id)
    mainViewModel.getReviews(id)
    val state = mainViewModel.state
    val details = state.detailsData
    var showAddReviewDialog by remember { mutableStateOf(false) }
    var isImageOpen by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.Black)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top color strip (like a header bar or status bar extension)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp) // height matching your padding
                    .background(MaterialTheme.colorScheme.primary) // or any color like Color.Blue
            )
        }

        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(details?.images?.picture_url ?: "")
                    .crossfade(true)
                    .build(),
                contentDescription = "Hotel Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
                    .clickable { isImageOpen = true }
            )
        }

        details?.let { it ->
            item {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold)) {
                                append(it.name)
                            }
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append("Price per night: ")
                            }
                            append("€${it.price}")
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append("Property Type: ")
                            }
                            append(it.property_type)
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append("Address: ")
                            }
                            append(it.address?.street ?: "(Street not provided)")
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Summary",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        color = Color.Blue
                    )

                    Text(
                        text = it.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
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
                        ReviewScoreItem("Overall Rating", scores.review_scores_rating/20)
                        ReviewScoreItem("Accuracy", scores.review_scores_accuracy)
                        ReviewScoreItem("Cleanliness", scores.review_scores_cleanliness)
                        ReviewScoreItem("Check-in", scores.review_scores_checkin)
                        ReviewScoreItem("Communication", scores.review_scores_communication)
                        ReviewScoreItem("Location", scores.review_scores_location)
                        ReviewScoreItem("Value", scores.review_scores_value/20)
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

        // Horizontal Scroll Reviews
        item {
            ReviewList(
                reviews = state.reviews,
                onSeeMoreClick = onNavigateToReviews,
                modifier = Modifier.padding(bottom = 16.dp)
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

    if (isImageOpen) {
        Dialog(onDismissRequest = { isImageOpen = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { isImageOpen = false },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = details?.images?.picture_url ?: "",
                    contentDescription = "Full Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(20000.dp)
                        .height(20000.dp)
                        .padding(16.dp)
                )
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (Review) -> Unit
) {
    var comments by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Review") },
        text = {
            Column {
                OutlinedTextField(
                    value = comments,
                    onValueChange = { comments = it },
                    label = { Text("Your Review") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val review = Review(
                        _id = UUID.randomUUID().toString(),
                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                        listing_id = "",
                        reviewer_id = "user123", // Will be replaced by actual user ID
                        reviewer_name = "User", // Will be replaced by actual username
                        comments = comments
                    )
                    onSubmit(review)
                },
                enabled = comments.isNotBlank()
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