package com.example.hotel_application.domain

import com.example.hotel_application.model.Data
import com.example.hotel_application.model.HotelList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("/listings")

    suspend fun getListing(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ):Response<List<Data>>
}