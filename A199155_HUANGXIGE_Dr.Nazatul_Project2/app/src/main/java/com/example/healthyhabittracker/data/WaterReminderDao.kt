package com.example.healthyhabittracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterReminderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(waterReminder: WaterReminder)

    @Update
    suspend fun update(waterReminder: WaterReminder)

    @Delete
    suspend fun delete(waterReminder: WaterReminder)

    @Query("SELECT * from water_reminder_table WHERE id = :id")
    fun getWaterReminder(id: Int): Flow<WaterReminder>

    @Query("SELECT * from water_reminder_table ORDER BY id ASC")
    fun getAllWaterReminders(): Flow<List<WaterReminder>>
}