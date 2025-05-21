package com.example.hotel_application.viewModel

import com.example.hotel_application.model.Data
import com.example.hotel_application.model.Details
import com.example.hotel_application.model.HotelList
import com.example.hotel_application.utils.RetrofitInstance
import retrofit2.Response
import androidx.lifecycle.viewModelScope

class Repository {
    suspend fun getHotelList(page: Int): Response<List<Data>> {
        return RetrofitInstance.listingApi.getListing(page = page, limit = 10)
    }
    suspend fun getDetailsById(_id: String): Response<List<Details>> {
        return RetrofitInstance.listingApi.getDetailsById(_id = _id)
    }

}