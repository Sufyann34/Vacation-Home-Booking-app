package com.example.hotel_application.domain

import com.example.hotel_application.model.Data
import com.example.hotel_application.model.Details
import com.example.hotel_application.model.LoginRequest
import com.example.hotel_application.model.SignupRequest
import com.example.hotel_application.model.LoginResponse
import com.example.hotel_application.model.SignupResponse
import com.example.hotel_application.model.Review
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @GET("/listings/search")
    suspend fun searchListings(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("name") name: String? = null,
        @Query("property_type") property_type: String? = null,
        @Query("min_price") minPrice: Float? = null,
        @Query("max_price") maxPrice: Float? = null
    ): Response<List<Data>>

    @GET("/listings/{item_id}")
    suspend fun getDetailsById(
        @Path("item_id")_id: String
    ): Response<List<Details>>

    @GET("/listings/{listing_id}/reviews")
    suspend fun getReviews(
        @Path("listing_id") listing_id: String,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<List<Review>>

    @POST("/listings/{listing_id}/reviews")
    suspend fun addReview(
        @Path("listing_id") listing_id: String,
        @Body review: Review,
        @Header("Authorization") authorization: String
    ): Response<Unit>

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