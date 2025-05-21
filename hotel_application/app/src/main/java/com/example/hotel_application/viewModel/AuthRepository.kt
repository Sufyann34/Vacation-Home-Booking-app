package com.example.hotel_application.viewModel

import com.example.hotel_application.model.LoginRequest
import com.example.hotel_application.utils.RetrofitInstance

class AuthRepository {
    suspend fun loginUser(request: LoginRequest) =
        RetrofitInstance.loginApi.login(request)
}
