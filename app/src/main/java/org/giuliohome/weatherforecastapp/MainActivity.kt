package org.giuliohome.weatherforecastapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.giuliohome.weatherforecastapp.ui.WeatherForecastScreen
import org.giuliohome.weatherforecastapp.ui.theme.WeatherForecastAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherForecastAppTheme {
                // Content setup with Scaffold
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Fetch and display forecast
                    WeatherForecastScreen(context = this,modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

