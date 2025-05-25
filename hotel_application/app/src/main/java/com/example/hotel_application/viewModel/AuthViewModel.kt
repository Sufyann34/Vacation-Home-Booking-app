package com.example.hotel_application.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_application.model.LoginRequest
import com.example.hotel_application.model.SignupRequest
import com.example.hotel_application.model.LoginResponse
import com.example.hotel_application.model.SignupResponse
import com.example.hotel_application.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginResponse = MutableStateFlow<Response<LoginResponse>?>(null)
    val loginResponse: StateFlow<Response<LoginResponse>?> = _loginResponse
    
    private val _signupResponse = MutableStateFlow<Response<SignupResponse>?>(null)
    val signupResponse: StateFlow<Response<SignupResponse>?> = _signupResponse

    fun initialize(context: Context) {
        UserManager.initialize(context)
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = repository.loginUser(LoginRequest(username, password))
            _loginResponse.value = response
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    UserManager.saveUserSession(loginResponse.token, loginResponse.user)
                }
            }
        }
    }

    fun signup(username: String, password: String, email: String) {
        viewModelScope.launch {
            val response = repository.signupUser(SignupRequest(username, password, email))
            _signupResponse.value = response
        }
    }

    fun logout() {
        UserManager.clearUserSession()
    }

    fun isLoggedIn(): Boolean = UserManager.isLoggedIn()
    fun getAuthToken(): String? = UserManager.getAuthToken()
    fun getUserId(): String? = UserManager.getUserId()
    fun getUsername(): String? = UserManager.getUsername()
}
