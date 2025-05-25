package com.example.hotel_application.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterTags(
    activeFilters: Map<String, Any>,
    onRemoveFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (activeFilters.isNotEmpty()) {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(activeFilters.toList()) { (key, value) ->
                FilterChip(
                    label = when (key) {
                        "name" -> "Search: $value"
                        "property_type" -> "Type: $value"
                        "minPrice" -> "Min: €$value"
                        "maxPrice" -> "Max: €$value"
                        else -> "$value"
                    },
                    onRemove = { onRemoveFilter(key) }
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
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