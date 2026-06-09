package com.example.healthyhabittracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthyhabittracker.ui.components.NotificationUtils
import com.example.healthyhabittracker.ui.theme.HealthyHabitTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        enableEdgeToEdge()
        setContent {
            HealthyHabitTrackerTheme{
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    BasicLayout()
                }
            }
        }
    }
    private fun createNotificationChannel(){
        val name = "Water Reminder"
        val descriptionText = "Channel for water intake reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("water_reminder_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NightPreview() {
    HealthyHabitTrackerTheme {
        BasicLayout()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LightPreview() {
    HealthyHabitTrackerTheme {
        BasicLayout()
    }
}