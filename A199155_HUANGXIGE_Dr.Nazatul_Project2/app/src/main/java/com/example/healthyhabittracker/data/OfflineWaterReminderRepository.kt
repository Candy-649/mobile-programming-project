package com.example.healthyhabittracker.data

import kotlinx.coroutines.flow.Flow

class OfflineWaterReminderRepository(private val waterReminderDao: WaterReminderDao): WaterReminderRepository {
    override fun getAllWaterReminders(): Flow<List<WaterReminder>> = waterReminderDao.getAllWaterReminders()

    override fun getWaterReminder(id: Int): Flow<WaterReminder?> = waterReminderDao.getWaterReminder(id)

    override suspend fun insertWaterReminder(waterReminder: WaterReminder) = waterReminderDao.insert(waterReminder)

    override suspend fun deleteWaterReminder(waterReminder: WaterReminder) = waterReminderDao.delete(waterReminder)

    override suspend fun updateWaterReminder(waterReminder: WaterReminder) = waterReminderDao.update(waterReminder)

}