package com.example.hotel_application.model

data class Review(
    val _id: String,
    val date: String,
    val listing_id: String,
    val reviewer_id: String,
    val reviewer_name: String,
    val comments: String
) 