package com.example.healthyhabittracker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthyhabittracker.ui.theme.HealthyHabitTrackerTheme

@Composable
fun WeatherScreen(
    temperature: String,
    feelsLikeTemperature: String,
    description: String,
    isDaytimeNow: Boolean,
    airQualityIndex: String,
    dominantPollutantText: String,
    advice: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 天气概览
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Current Weather",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = temperature,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "(Feels like $feelsLikeTemperature)",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (isDaytimeNow) "Daytime" else "Nighttime",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 空气质量
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Air Quality",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = airQualityIndex,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Dominant Pollutant: $dominantPollutantText",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // 出行建议
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Advice",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = advice,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherNightPreview() {
    HealthyHabitTrackerTheme {
        WeatherScreen(
            temperature = "22°C",
            feelsLikeTemperature = "21°C",
            description = "Partly Cloudy",
            isDaytimeNow = true,
            airQualityIndex = "52 (Moderate)",
            dominantPollutantText = "NO2",
            advice = "Good day to go out",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun WeatherLightPreview() {
    HealthyHabitTrackerTheme {
        WeatherScreen(
            temperature = "22°C",
            feelsLikeTemperature = "21°C",
            description = "Partly Cloudy",
            isDaytimeNow = true,
            airQualityIndex = "52 (Moderate)",
            dominantPollutantText = "NO2",
            advice = "Good day to go out",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
