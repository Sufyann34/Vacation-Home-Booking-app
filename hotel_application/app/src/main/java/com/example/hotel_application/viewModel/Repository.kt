package com.example.hotel_application.viewModel

import com.example.hotel_application.model.Data
import com.example.hotel_application.model.HotelList
import com.example.hotel_application.utils.RetrofitInstance
import retrofit2.Response

class Repository {
    suspend fun getHotelList(page: Int): Response<List<Data>> {
        val skip = (page - 1) * 10
        return RetrofitInstance.listingApi.getListing(skip = skip, limit = 10)
    }
}