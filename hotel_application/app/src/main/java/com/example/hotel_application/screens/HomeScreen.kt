package com.example.hotel_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hotel_application.components.ListingCard
import com.example.hotel_application.components.FilterTags
import com.example.hotel_application.viewModel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val state = viewModel.state
    var showFilters by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("") }
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }
    
    val listState = rememberLazyListState()

    // Update local state when filters change in ViewModel
    LaunchedEffect(state.searchQuery) {
        searchQuery = state.searchQuery
    }
    LaunchedEffect(state.propertyType) {
        propertyType = state.propertyType
    }
    LaunchedEffect(state.minPrice) {
        minPrice = state.minPrice?.toString() ?: ""
    }
    LaunchedEffect(state.maxPrice) {
        maxPrice = state.maxPrice?.toString() ?: ""
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= state.hotels.size - 5) {
                    viewModel.loadMoreListings()
                }
            }
    }

    fun applyFilters() {
        val minPriceFloat = minPrice.toFloatOrNull()
        val maxPriceFloat = maxPrice.toFloatOrNull()
        
        viewModel.searchListings(
            name = searchQuery.takeIf { it.isNotBlank() },
            propertyType = propertyType.takeIf { it.isNotBlank() },
            minPrice = minPriceFloat,
            maxPrice = maxPriceFloat
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = { Text("FeinBleiben") },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "Toggle filters"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it
                    if (it.isBlank()) {
                        viewModel.clearAllFilters()
                    }
                },
                onSearch = { applyFilters() },
                placeholder = { Text("Search hotels...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Active filters
            FilterTags(
                activeFilters = state.activeFilters,
                onRemoveFilter = { key -> viewModel.removeFilter(key) }
            )

            // Filters section
            if (showFilters) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Filters",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = propertyType,
                            onValueChange = { propertyType = it },
                            onSearch = { applyFilters() },
                            placeholder = { Text("Property Type") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = minPrice,
                                onValueChange = { 
                                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                        minPrice = it
                                    }
                                },
                                onSearch = { applyFilters() },
                                placeholder = { Text("Min Price") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = maxPrice,
                                onValueChange = { 
                                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                        maxPrice = it
                                    }
                                },
                                onSearch = { applyFilters() },
                                placeholder = { Text("Max Price") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = { applyFilters() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text("Apply Filters")
                        }
                    }
                }
            }

            // Hotel list with lazy loading
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(state.hotels) { hotel ->
                    ListingCard(
                        hotel = hotel,
                        navController = navController as NavHostController
                    )
                }
                
                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun CustomFilterChip(
    label: String,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove filter",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    placeholder: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.small,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        shape = shape,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
            onSearch = { onSearch() }
        )
    )
}

