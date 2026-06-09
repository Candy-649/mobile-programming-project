package com.example.healthyhabittracker.ui.screen

import androidx.compose.runtime.Composable
import com.example.healthyhabittracker.data.DataSource
import com.example.healthyhabittracker.ui.components.SettingsSingleChoiceSection

@Composable
fun NotificationTypeScreen(
    title: String,
    options: List<DataSource.NotificationType>,
    onTypeSelected: (DataSource.NotificationType) -> Unit
){
    SettingsSingleChoiceSection(
        title = title,
        options = options,
        selectedIndex = 0
    ) { value ->
        onTypeSelected(value)
    }
}