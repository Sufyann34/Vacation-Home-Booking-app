package com.example.hotel_application.model

data class Details (
    val _id: String,
    val name: String,
    val price: Float,
    val property_type: String,
    val images: Images,
    val summary: String,
    val address: Address?
)

data class Address (
    val street: String?,
    val country: String?
)