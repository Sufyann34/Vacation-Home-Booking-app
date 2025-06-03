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
import android.util.Log

class MainViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(ScreenState())
    private var _currentPage = 1
    private val pageSize = 10
    var hasMorePages by mutableStateOf(true)
        private set
    private var isSearching = false
    private var lastResponseSize = 0

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
            Log.e("MainViewModel", "Error clearing user session: ${e.message}")
        }
        state = ScreenState()
        _currentPage = 1
        hasMorePages = true
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
                Log.e("MainViewModel", "Error getting details: ${e.message}")
            }
        }
    }

    private fun updateActiveFilters(
        searchQuery: String? = null,
        propertyType: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null,
        sortBy: String? = null,
        sortOrder: Int? = null
    ) {
        val currentFilters = state.activeFilters.toMutableMap()
        
        searchQuery?.let { 
            if (it.isNotBlank()) currentFilters["name"] = it 
            else currentFilters.remove("name")
        }
        if (searchQuery.isNullOrBlank()) {
            currentFilters.remove("name")
        }
        propertyType?.let {
            if (it.isNotBlank()) currentFilters["property_type"] = it
            else currentFilters.remove("property_type")
        }
        if (propertyType.isNullOrBlank()) {
            currentFilters.remove("property_type")
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
        sortBy: String? = null,
        sortOrder: Int? = null,
        reset: Boolean = true
    ) {
        if (isSearching) return

        if (reset) {
            _currentPage = 1
            hasMorePages = true
            lastResponseSize = 0
            state = state.copy(hotels = emptyList())
            Log.d("Pagination", "Reset state - cleared hotels list")
        }

        if (!hasMorePages && !reset) {
            Log.d("Pagination", "No more pages available, skipping search")
            return
        }

        state = state.copy(
            searchQuery = name ?: "",
            propertyType = propertyType ?: "",
            minPrice = minPrice,
            maxPrice = maxPrice
        )

        updateActiveFilters(name, propertyType, minPrice, maxPrice, sortBy, sortOrder)

        viewModelScope.launch {
            try {
                isSearching = true
                state = state.copy(isLoading = true)
                Log.d("Pagination", "Fetching page $_currentPage with ${state.hotels.size} existing items")
                val response = repository.searchListings(
                    page = _currentPage,
                    limit = pageSize,
                    name = name,
                    propertyType = propertyType,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    sortBy = sortBy,
                    sortOrder = sortOrder
                )
                if (response.isSuccessful) {
                    response.body()?.let { hotels ->
                        lastResponseSize = hotels.size
                        Log.d("Pagination", "Received ${hotels.size} items for page $_currentPage")
                        
                        if (hotels.isEmpty() || hotels.size < pageSize) {
                            hasMorePages = false
                            Log.d("Pagination", "No more pages available")
                        }
                        
                        val updatedHotels = if (reset) {
                            hotels
                        } else {
                            val existingHotels = state.hotels.associateBy { it._id }
                            state.hotels + hotels.filter { !existingHotels.containsKey(it._id) }
                        }
                        
                        Log.d("Pagination", "Updating state with ${updatedHotels.size} total items (${if (reset) "reset" else "appended"})")
                        state = state.copy(
                            hotels = updatedHotels,
                            isLoading = false
                        )
                        
                        if (hasMorePages) {
                            _currentPage++
                            Log.d("Pagination", "Incrementing page to $_currentPage")
                        }
                    } ?: run {
                        hasMorePages = false
                        state = state.copy(isLoading = false)
                        Log.d("Pagination", "Empty response body")
                    }
                } else {
                    hasMorePages = false
                    state = state.copy(isLoading = false)
                    Log.d("Pagination", "Unsuccessful response: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error searching listings: ${e.message}")
                hasMorePages = false
                state = state.copy(isLoading = false)
            } finally {
                isSearching = false
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

    fun clearAllFilters() {
        state = state.copy(
            activeFilters = emptyMap(),
            searchQuery = "",
            propertyType = "",
            minPrice = null,
            maxPrice = null,
            hotels = emptyList()
        )
        searchListings()
    }

    fun loadMoreListings() {
        if (hasMorePages && !isSearching) {
            Log.d("Pagination", "Loading more listings. Current page: $_currentPage, Current items: ${state.hotels.size}")
            if (lastResponseSize == pageSize) {
                searchListings(
                    name = state.searchQuery,
                    propertyType = state.propertyType,
                    minPrice = state.minPrice,
                    maxPrice = state.maxPrice,
                    reset = false
                )
            } else {
                hasMorePages = false
                Log.d("Pagination", "Stopping pagination - last response was not a full page")
            }
        } else {
            Log.d("Pagination", "Skipping load more - isLoading: ${state.isLoading}, hasMorePages: $hasMorePages, isSearching: $isSearching, items: ${state.hotels.size}")
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
                Log.e("MainViewModel", "Error getting reviews: ${e.message}")
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

