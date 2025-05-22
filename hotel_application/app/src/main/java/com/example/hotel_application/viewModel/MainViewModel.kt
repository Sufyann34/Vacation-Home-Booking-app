package com.example.hotel_application.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_application.model.Data
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.hotel_application.model.Details

class MainViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(ScreenState())
    var id by mutableStateOf("")

    var isLoggedIn by mutableStateOf(false)
        private set

    init {
//         Initially load the first page of hotels
        loadHotels(state.page)
    }

    fun login() {
        isLoggedIn = true
        loadHotels(state.page) // Load hotels after login
    }

    fun logout() {
        isLoggedIn = false
        state = ScreenState() // Reset UI state
    }

    fun getDetailsById(_id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailsById(_id = _id)

                if (response.isSuccessful) {
                    response.body()?.firstOrNull()?.let { details ->
                        state = state.copy(detailsData = details)
                        println("Details loaded: $details")
                    } ?: println("Details list is empty")
                } else {
                    println("Failed to load details: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Exception in getDetailsById: ${e.message}")
            }
        }
    }


    // Function to load hotels based on the current page
    private fun loadHotels(page: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getHotelList(page)

                if (response.isSuccessful) {
                    val hotels = response.body()
                    if (!hotels.isNullOrEmpty()) {
                        println("API returned ${hotels.size} hotels")
                        state = state.copy(hotels = hotels, page = page)
                    } else {
                        println("API returned empty or null list")
                    }
                } else {
                    println("API error: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
    }

    // Navigate to the next page (increment page)
    fun nextPage() {
        loadHotels(state.page + 1)
    }

    // Navigate to the previous page (decrement page)
    fun previousPage() {
        if (state.page > 1) {
            loadHotels(state.page - 1)
        }
    }
}

data class ScreenState(
    val hotels: List<Data> = emptyList(),
    val page: Int = 1,
    val detailsData: Details? = null
)
