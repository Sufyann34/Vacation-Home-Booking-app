package com.example.smartphone_application.model

data class listingModel(
    val id: Int,
    val name: String,
    val location: String,
    val pricePerNight: String,
    val imageUrl: String? = null
)
