package com.example.hotel_application.model

data class Data (
    val _id: String,
    val name: String,
    val price: Int,
    val property_type: String,
    val images: Images
)

data class Images(
    val thumbnail_url: String,
    val medium_url: String,
    val picture_url: String,
    val xl_picture_url: String
)