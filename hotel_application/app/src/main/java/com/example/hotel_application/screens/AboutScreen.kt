package com.example.hotel_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ✅ Variables for easy customization
private val appTitle = "Hotel Booking App"
private val appVersion = "v1.0.0"
private val appDescription = "This app helps users find and book hotels with ease. It offers listings, filtering, and secure authentication."

private val featuresHeading = "Key Features"
private val featuresList = listOf(
    "Browse hotels with high-quality images",
    "Read detailed hotel descriptions and prices",
    "Authenticate securely (Login & Signup)",
    "Smooth navigation with bottom navigation bar",
    "Paginated hotel listings"
)

private val techStackHeading = "Built With"
private val techList = listOf(
    "Jetpack Compose (UI Toolkit)",
    "Kotlin + Coroutines",
    "Navigation Component",
    "MVVM Architecture",
    "Retrofit for Networking"
)

private val creditsHeading = "Developers Name"
private val developerName = "Ahmed Alam\n" + "Syed Muhammad Hasan Naqvi\n" + "Sufyan Khan\n" + "Ali bawa"
private val developerEmail = "info@vacationHomeBooking.com"

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = appTitle, style = MaterialTheme.typography.headlineLarge)
        Text(text = appVersion, style = MaterialTheme.typography.labelLarge)

        Divider(thickness = 1.dp)

        SectionHeader("About the App")
        Text(text = appDescription, style = MaterialTheme.typography.bodyLarge)

        Divider(thickness = 1.dp)

        SectionHeader(featuresHeading)
        BulletList(featuresList)

        Divider(thickness = 1.dp)

        SectionHeader(techStackHeading)
        BulletList(techList)

        Divider(thickness = 1.dp)

        SectionHeader(creditsHeading)
        Text("$developerName", style = MaterialTheme.typography.bodyLarge)
        Text("Email: $developerEmail", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun BulletList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEach { item ->
            Row {
                Text("• ", fontSize = 20.sp)
                Text(item, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}
