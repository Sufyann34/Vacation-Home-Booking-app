package com.example.hotel_application.model

data class Details (
    val _id: String,
    val name: String,
    val price: Float,
    val property_type: String,
    val images: Images,
    val summary: String,
    val address: Address?,
    val reviews: List<Review>? = null,
    val review_scores: ReviewScores? = null
)

data class Address (
    val street: String?,
    val country: String?
)

data class ReviewScores (
    val review_scores_accuracy: Int,
    val review_scores_cleanliness: Int,
    val review_scores_checkin: Int,
    val review_scores_communication: Int,
    val review_scores_location: Int,
    val review_scores_value: Int,
    val review_scores_rating: Int
)