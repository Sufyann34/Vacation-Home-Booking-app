package com.example.hotel_application.viewModel

import android.util.Log
import com.example.hotel_application.model.Data
import com.example.hotel_application.model.Details
import com.example.hotel_application.model.Review
import com.example.hotel_application.utils.RetrofitInstance
import com.example.hotel_application.utils.UserManager
import retrofit2.Response

class Repository {
    suspend fun searchListings(
        page: Int = 1,
        limit: Int = 10,
        name: String? = null,
        propertyType: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null,
        sortBy: String? = null,
        sortOrder: Int? = null
    ): Response<List<Data>> {
        Log.d("Repository", "Making API call - page: $page, limit: $limit")
        return RetrofitInstance.listingApi.searchListings(
            page = page,
            limit = limit,
            name = name,
            property_type = propertyType,
            minPrice = minPrice,
            maxPrice = maxPrice,
            sort_by = sortBy,
            sort_order = sortOrder
        )
    }

    suspend fun getDetailsById(_id: String): Response<List<Details>> {
        return RetrofitInstance.listingApi.getDetailsById(_id = _id)
    }

    suspend fun getReviews(listing_id: String, page: Int? = null, limit: Int? = null): Response<List<Review>> {
        return RetrofitInstance.listingApi.getReviews(listing_id = listing_id, page = page, limit = limit)
    }

    suspend fun addReview(listing_id: String, review: Review): Response<Unit> {
        val token = UserManager.getAuthToken()
        return RetrofitInstance.listingApi.addReview(
            listing_id = listing_id,
            review = review,
            authorization = "Token $token"
        )
    }
}