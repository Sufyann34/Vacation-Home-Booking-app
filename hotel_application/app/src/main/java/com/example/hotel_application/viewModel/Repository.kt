package com.example.hotel_application.viewModel

import com.example.hotel_application.model.Data
import com.example.hotel_application.model.Details
import com.example.hotel_application.model.HotelList
import com.example.hotel_application.model.Review
import com.example.hotel_application.utils.RetrofitInstance
import retrofit2.Response
import androidx.lifecycle.viewModelScope

class Repository {
    suspend fun searchListings(
        limit: Int = 10,
        name: String? = null,
        propertyType: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null
    ): Response<List<Data>> {
        return RetrofitInstance.listingApi.searchListings(
            limit = limit,
            name = name,
            property_type = propertyType,
            minPrice = minPrice,
            maxPrice = maxPrice
        )
    }

    suspend fun getHotelList(page: Int): Response<List<Data>> {
        return RetrofitInstance.listingApi.getListing(page = page, limit = 10)
    }

    suspend fun getDetailsById(_id: String): Response<List<Details>> {
        return RetrofitInstance.listingApi.getDetailsById(_id = _id)
    }

    suspend fun getReviews(listing_id: String, page: Int? = null, limit: Int? = null): Response<List<Review>> {
        return RetrofitInstance.listingApi.getReviews(listing_id = listing_id, page = page, limit = limit)
    }

    suspend fun addReview(listing_id: String, review: Review): Response<Unit> {
        return RetrofitInstance.listingApi.addReview(listing_id = listing_id, review = review)
    }

    suspend fun deleteReview(listing_id: String, review_id: String): Response<Unit> {
        return RetrofitInstance.listingApi.deleteReview(listing_id = listing_id, review_id = review_id)
    }
}