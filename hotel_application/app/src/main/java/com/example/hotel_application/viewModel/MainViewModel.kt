package com.example.hotel_application.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_application.model.Data
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.hotel_application.model.Details
import com.example.hotel_application.model.Review

class MainViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(ScreenState())
    var id by mutableStateOf("")
    private var currentOffset = 0
    private val pageSize = 20

    var isLoggedIn by mutableStateOf(false)
        private set

    init {
        searchListings()
    }

    fun login() {
        isLoggedIn = true
        searchListings()
    }

    fun logout() {
        isLoggedIn = false
        state = ScreenState()
        currentOffset = 0
    }

    fun getDetailsById(_id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailsById(_id = _id)
                if (response.isSuccessful) {
                    response.body()?.firstOrNull()?.let { details ->
                        state = state.copy(detailsData = details)
                    }
                }
            } catch (e: Exception) {
                println("Exception in getDetailsById: ${e.message}")
            }
        }
    }

    fun searchListings(
        name: String? = null,
        propertyType: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null,
        reset: Boolean = true
    ) {
        if (reset) {
            currentOffset = 0
        }
        
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)
                
                // Only include non-empty filters
                val filters = mutableMapOf<String, Any>()
                if (!name.isNullOrBlank()) filters["name"] = name
                if (!propertyType.isNullOrBlank()) {
                    filters["property_type"] = propertyType.trim()
                }
                if (minPrice != null && minPrice > 0) filters["minPrice"] = minPrice
                if (maxPrice != null && maxPrice > 0) filters["maxPrice"] = maxPrice
                
                val response = repository.searchListings(
                    limit = pageSize,
                    name = if (filters.containsKey("name")) name else null,
                    propertyType = if (filters.containsKey("property_type")) propertyType else null,
                    minPrice = if (filters.containsKey("minPrice")) minPrice else null,
                    maxPrice = if (filters.containsKey("maxPrice")) maxPrice else null
                )

                if (response.isSuccessful) {
                    val hotels = response.body()
                    if (!hotels.isNullOrEmpty()) {
                        state = state.copy(
                            hotels = if (reset) hotels else state.hotels + hotels,
                            searchQuery = name ?: "",
                            propertyType = propertyType ?: "",
                            minPrice = minPrice,
                            maxPrice = maxPrice,
                            isLoading = false,
                            activeFilters = filters
                        )
                        currentOffset += hotels.size
                    } else {
                        state = state.copy(
                            hotels = if (reset) emptyList() else state.hotels,
                            searchQuery = name ?: "",
                            propertyType = propertyType ?: "",
                            minPrice = minPrice,
                            maxPrice = maxPrice,
                            isLoading = false,
                            activeFilters = filters
                        )
                    }
                } else {
                    state = state.copy(
                        hotels = if (reset) emptyList() else state.hotels,
                        searchQuery = name ?: "",
                        propertyType = propertyType ?: "",
                        minPrice = minPrice,
                        maxPrice = maxPrice,
                        isLoading = false,
                        activeFilters = filters
                    )
                }
            } catch (e: Exception) {
                println("Exception in searchListings: ${e.message}")
                state = state.copy(
                    hotels = if (reset) emptyList() else state.hotels,
                    searchQuery = name ?: "",
                    propertyType = propertyType ?: "",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    isLoading = false,
                    activeFilters = state.activeFilters
                )
            }
        }
    }

    fun removeFilter(key: String) {
        when (key) {
            "property_type" -> searchListings(
                name = state.searchQuery,
                propertyType = null,
                minPrice = state.minPrice,
                maxPrice = state.maxPrice
            )
            "name" -> searchListings(
                name = null,
                propertyType = state.propertyType,
                minPrice = state.minPrice,
                maxPrice = state.maxPrice
            )
            "minPrice" -> searchListings(
                name = state.searchQuery,
                propertyType = state.propertyType,
                minPrice = null,
                maxPrice = state.maxPrice
            )
            "maxPrice" -> searchListings(
                name = state.searchQuery,
                propertyType = state.propertyType,
                minPrice = state.minPrice,
                maxPrice = null
            )
        }
    }

    fun loadMoreListings() {
        if (!state.isLoading) {
            searchListings(
                name = state.searchQuery,
                propertyType = state.propertyType,
                minPrice = state.minPrice,
                maxPrice = state.maxPrice,
                reset = false
            )
        }
    }

    fun getReviews(listing_id: String, page: Int? = null, limit: Int? = null) {
        viewModelScope.launch {
            try {
                val response = repository.getReviews(listing_id, page, limit)
                if (response.isSuccessful) {
                    response.body()?.let { reviews ->
                        state = state.copy(reviews = reviews)
                    }
                }
            } catch (e: Exception) {
                println("Exception in getReviews: ${e.message}")
            }
        }
    }

    fun addReview(listing_id: String, review: Review) {
        viewModelScope.launch {
            try {
                state = state.copy(isAddingReview = true, reviewError = null)
                val response = repository.addReview(listing_id, review)
                if (response.isSuccessful) {
                    // Refresh reviews after adding
                    getReviews(listing_id)
                } else {
                    state = state.copy(reviewError = "Failed to add review")
                }
            } catch (e: Exception) {
                state = state.copy(reviewError = e.message ?: "Unknown error occurred")
            } finally {
                state = state.copy(isAddingReview = false)
            }
        }
    }

    fun deleteReview(listing_id: String, review_id: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteReview(listing_id, review_id)
                if (response.isSuccessful) {
                    // Refresh reviews after deleting
                    getReviews(listing_id)
                }
            } catch (e: Exception) {
                println("Exception in deleteReview: ${e.message}")
            }
        }
    }
}

data class ScreenState(
    val hotels: List<Data> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val propertyType: String = "",
    val minPrice: Float? = null,
    val maxPrice: Float? = null,
    val detailsData: Details? = null,
    val activeFilters: Map<String, Any> = emptyMap(),
    val reviews: List<Review> = emptyList(),
    val isAddingReview: Boolean = false,
    val reviewError: String? = null
)

