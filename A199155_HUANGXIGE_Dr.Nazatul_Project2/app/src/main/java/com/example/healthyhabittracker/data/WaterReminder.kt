package com.example.healthyhabittracker.data

import androidx.compose.runtime.ComposableOpenTarget
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_reminder_table")
data class WaterReminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "reminder_hour")
    val hour: String,

    @ColumnInfo(name = "reminder_minute")
    val minute: String,

    @ColumnInfo(name = "repeat_days")
    val repeatDays: List<Int> = emptyList(),

    @ColumnInfo(name = "notification_type")
    val notificationType: List<DataSource.NotificationType> = listOf(DataSource.NotificationType.SOUND),

    @ColumnInfo(name = "notification_label")
    val label: String = "It's time to drink water."
)
