package com.example.hotel_application.utils

import com.example.hotel_application.domain.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val listingApi: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(Util.ListingBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}