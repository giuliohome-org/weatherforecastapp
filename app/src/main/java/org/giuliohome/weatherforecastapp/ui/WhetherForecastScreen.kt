package org.giuliohome.weatherforecastapp.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.giuliohome.weatherforecastapp.Forecast
import org.giuliohome.weatherforecastapp.RetrofitClient
import org.giuliohome.weatherforecastapp.WeatherPreferences


// Parse forecast response

fun parseForecast(forecast: String): Forecast {
    val regex = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}): ([\\d.-]+)°C, (.+)".toRegex()
    val matchResult = regex.find(forecast)

    return if (matchResult != null) {
        val (date, temperature, weather) = matchResult.destructured
        Forecast(date, "$temperature°C", weather)
    } else {
        Forecast("Unknown", "N/A", "Unknown")
    }
}

@Composable
fun WeatherForecastScreen(context: Context, modifier: Modifier = Modifier) {
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var forecastList by remember { mutableStateOf<List<Forecast>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load saved city and country on first launch
    LaunchedEffect(Unit) {
        val (savedCity, savedCountry) = WeatherPreferences.getCityAndCountry(context)
        city = savedCity
        country = savedCountry
    }

    // Coroutine to fetch data
    LaunchedEffect(isLoading) {
        if (isLoading) {
            if (city.isNotBlank() && country.isNotBlank()) {
                try {
                    errorMessage = null
                    val response = RetrofitClient.weatherApi.getForecast(city, country)
                    forecastList = response.map { parseForecast(it) }
                    WeatherPreferences.saveCityAndCountry(context, city, country)
                } catch (e: Exception) {
                    errorMessage = "Error fetching data"
                } finally {
                    isLoading = false
                }
            } else {
                errorMessage = "Please enter both city and country"
                isLoading = false
            }
        }
    }

    Column(modifier = modifier) {
        // Input fields for city and country
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { isLoading = true }) {
            Text("Refresh")
        }
        // Use Box to manage alignment for the loading indicator and error message
        Box(modifier = modifier.fillMaxSize()) {
            if (isLoading) {
                // Show loading indicator
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                // Show error message
                Text(
                    text = errorMessage ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                // Show the forecast list
                LazyColumn(modifier = modifier) {
                    items(forecastList) { forecast ->
                        ForecastItem(forecast)
                    }
                }
            }
        }
    }
}


