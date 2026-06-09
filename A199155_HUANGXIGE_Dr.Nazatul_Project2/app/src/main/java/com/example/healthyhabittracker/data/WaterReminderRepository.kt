package com.example.healthyhabittracker.data

import kotlinx.coroutines.flow.Flow

interface WaterReminderRepository {

    fun getAllWaterReminders(): Flow<List<WaterReminder>>

    fun getWaterReminder(id: Int): Flow<WaterReminder?>

    suspend fun insertWaterReminder(waterReminder: WaterReminder)

    suspend fun deleteWaterReminder(waterReminder: WaterReminder)

    suspend fun updateWaterReminder(waterReminder: WaterReminder)
}