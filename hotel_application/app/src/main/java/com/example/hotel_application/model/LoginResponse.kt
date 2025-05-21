package com.example.hotel_application.model

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class SignupResponse(
    val username: String,
    val password: String,
    val email: String
)
