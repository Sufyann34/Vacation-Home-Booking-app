package com.example.hotel_application.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_application.model.Data
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.hotel_application.model.Images

class MainViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(ScreenState())

    init {
        viewModelScope.launch {
            try {
                val response = repository.getHotelList(state.page)

                if (response.isSuccessful) {
                    val hotels = response.body()
                    if (!hotels.isNullOrEmpty()) {
                        println("✅ API returned ${hotels.size} hotels")
                        state = state.copy(hotels = hotels)
                    } else {
                        println("❌ API returned empty or null list")
                    }
                } else {
                    println("❌ API error: ${response.code()}")
                }

            } catch (e: Exception) {
                println("🔥 Exception: ${e.message}")
            }
        }
    }
}

data class ScreenState(
    val hotels: List<Data> = emptyList(),
    val page: Int = 1
)