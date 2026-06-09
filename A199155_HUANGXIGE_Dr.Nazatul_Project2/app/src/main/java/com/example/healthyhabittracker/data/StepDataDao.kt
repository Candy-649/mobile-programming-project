package com.example.healthyhabittracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stepData: StepData)

    @Update
    suspend fun update(stepData: StepData)

    @Delete
    suspend fun delete(stepData: StepData)

    @Query("SELECT * from step_count_table WHERE date = :date")
    fun getStepData(date: String): Flow<StepData?>

    @Query("SELECT * from step_count_table ORDER BY date ASC")
    fun getAllStepData(): Flow<List<StepData>>

    @Query("SELECT * FROM step_count_table ORDER BY date DESC LIMIT 1")
    suspend fun getLastStepData(): StepData?

    @Query("SELECT * FROM step_count_table WHERE date < :todayDate")
    suspend fun getHistoricalSteps(todayDate: String): List<StepData>

}
