package com.example.hotel_application.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_application.model.Data
import com.example.hotel_application.model.Details
import com.example.hotel_application.model.Review
import com.example.hotel_application.utils.UserManager
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(ScreenState())
    var id by mutableStateOf("")
    private var currentOffset = 0
    private val pageSize = 20

    var isLoggedIn by mutableStateOf(false)
        private set

    init {
        try {
            isLoggedIn = UserManager.isLoggedIn()
        } catch (e: Exception) {
            isLoggedIn = false
        }
        searchListings()
    }

    fun login() {
        isLoggedIn = true
        searchListings()
    }

    fun logout() {
        isLoggedIn = false
        try {
            UserManager.clearUserSession()
        } catch (e: Exception) {
            println("Exception in getDetailsById: ${e.message}")
        }
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

    private fun updateActiveFilters(
        searchQuery: String? = null,
        propertyType: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null
    ) {
        val currentFilters = state.activeFilters.toMutableMap()
        
        searchQuery?.let { 
            if (it.isNotBlank()) currentFilters["name"] = it 
            else currentFilters.remove("name")
        }
        
        propertyType?.let { 
            if (it.isNotBlank()) currentFilters["property_type"] = it 
            else currentFilters.remove("property_type")
        }
        
        minPrice?.let { currentFilters["minPrice"] = it } ?: currentFilters.remove("minPrice")
        maxPrice?.let { currentFilters["maxPrice"] = it } ?: currentFilters.remove("maxPrice")
        
        state = state.copy(activeFilters = currentFilters)
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
            state = state.copy(hotels = emptyList())
        }

        // Update state with new search parameters
        state = state.copy(
            searchQuery = name ?: "",
            propertyType = propertyType ?: "",
            minPrice = minPrice,
            maxPrice = maxPrice
        )

        // Update active filters
        updateActiveFilters(name, propertyType, minPrice, maxPrice)

        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)
                val response = repository.searchListings(
                    limit = pageSize,
                    name = name,
                    propertyType = propertyType,
                    minPrice = minPrice,
                    maxPrice = maxPrice
                )
                if (response.isSuccessful) {
                    response.body()?.let { hotels ->
                        state = state.copy(
                            hotels = if (reset) hotels else state.hotels + hotels,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                println("Exception in searchListings: ${e.message}")
                state = state.copy(isLoading = false)
            }
        }
    }

    fun removeFilter(key: String) {
        when (key) {
            "name" -> searchListings(
                name = null,
                propertyType = state.propertyType,
                minPrice = state.minPrice,
                maxPrice = state.maxPrice
            )
            "property_type" -> searchListings(
                name = state.searchQuery,
                propertyType = null,
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
                val userId = try {
                    UserManager.getUserId()
                } catch (e: Exception) {
                    null
                }
                val username = try {
                    UserManager.getUsername()
                } catch (e: Exception) {
                    null
                }
                
                if (userId == null || username == null) {
                    state = state.copy(reviewError = "User not logged in")
                    return@launch
                }

                val reviewWithUser = review.copy(
                    reviewer_id = userId,
                    reviewer_name = username
                )

                val response = repository.addReview(listing_id, reviewWithUser)
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

