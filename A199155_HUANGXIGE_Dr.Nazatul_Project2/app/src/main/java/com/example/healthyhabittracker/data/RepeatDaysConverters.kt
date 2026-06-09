package com.example.healthyhabittracker.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class RepeatDaysConverters {
    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return Json.decodeFromString(value)
    }
}