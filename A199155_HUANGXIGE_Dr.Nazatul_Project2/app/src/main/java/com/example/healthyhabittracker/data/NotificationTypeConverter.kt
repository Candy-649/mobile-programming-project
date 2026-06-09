package com.example.healthyhabittracker.data

import androidx.room.TypeConverter

class NotificationTypeConverter {
    @TypeConverter
    fun fromList(value: List<DataSource.NotificationType>): String {
        return value.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toList(value: String): List<DataSource.NotificationType> {
        if (value.isBlank()) return emptyList()
        return value.split(",").map { DataSource.NotificationType.valueOf(it) }
    }
}