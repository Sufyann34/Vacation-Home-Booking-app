package com.example.hotel_application.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hotel_application.navigation.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    val currentItem = BottomNavItem.fromRoute(currentRoute)

    currentItem?.let {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BottomNavItem.items().forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    },
                    label = {
                        Text(text = item.label)
                    },
                    selected = item == currentItem,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    } ?: run {
        Log.d("Bottom Navigation Bar", "inside run")
    }
}
