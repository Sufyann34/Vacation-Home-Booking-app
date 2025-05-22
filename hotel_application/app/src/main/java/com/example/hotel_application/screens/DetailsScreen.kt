package com.example.hotel_application.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hotel_application.viewModel.MainViewModel

@Composable
fun DetailsScreen (id: String){
    val mainViewModel = viewModel<MainViewModel>()
    mainViewModel.id = id
    mainViewModel.getDetailsById(id)
    val state = mainViewModel.state
    val details = state.detailsData

    Text(text = details.toString())
}