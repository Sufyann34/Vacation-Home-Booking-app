package com.example.hotel_application.model
import com.example.hotel_application.model.User

data class SignupResponse(
    val token: String,
    val user: User
)