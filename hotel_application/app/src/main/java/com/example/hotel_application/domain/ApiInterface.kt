package com.example.hotel_application.domain

import com.example.hotel_application.model.Data
import com.example.hotel_application.model.Details
import com.example.hotel_application.model.HotelList
import com.example.hotel_application.model.LoginRequest
import com.example.hotel_application.model.SignupRequest
import com.example.hotel_application.model.LoginResponse
import com.example.hotel_application.model.SignupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiInterface {
    @GET("/listings")
    suspend fun getListing(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ):Response<List<Data>>

    @GET("/listings/{item_id}")
    suspend fun getDetailsById(
        @Path("item_id")_id: String
    ): Response<List<Details>>

    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("/signup")
    suspend fun signup(
        @Body signupRequest: SignupRequest
    ): Response<SignupResponse>  // Or your desired response type

    @GET("/verify")
    suspend fun verify(
        @Header("Authorization") token: String
    ): Response<Unit>  // Or replace with your actual response model
}