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


private val appTitle = "FeinBleiben"
private val appVersion = "v1.0.0"
private val appDescription = "The Vacation Home Booking project is based on a microservice architecture. It was developed as part of the \"Programming Distributed Systems\" course. The goal is to create a robust and scalable platform for booking vacation homes."

private val featuresHeading = "Key Features"
private val featuresList = listOf(
    "Users can register and log into their accounts.",
    "Users can browse a list of hotels and view detailed information by selecting individual listings.",
    "A Command Line Interface (CLI) is available for administrators to manage hotel listings and moderate user reviews.",
    "Users have the ability to submit reviews for hotels."
)

private val techStackHeading = "Built With"
private val techList = listOf(
    "Kotlin, Jetpeck Compose",
    "Python; FastAPI, Django Framework",
    "Docker",
    "RESTful Architecture",
    "Retrofit for Networking"
)

private val creditsHeading = "Developers Name"
private val developerName = "Ahmed Alam\n" + "Syed Mohammad Hasan Naqvi\n" + "Muhammad Sufyan\n" + "Ali Bawa"
private val developerEmail = "Group 08, Programming Distributed Systems (SoSe25)"

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = appTitle, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(top = 16.dp))
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
        Text("$developerEmail", style = MaterialTheme.typography.bodyLarge)
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
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
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
