package com.example.healthyhabittracker.data

import kotlinx.coroutines.flow.Flow

interface StepDataRepository {

    fun getAllStepData(): Flow<List<StepData>>

    fun getStepData(date: String): Flow<StepData?>

    suspend fun getLastStepData(): StepData?

    suspend fun insertStepData(stepData: StepData)

    suspend fun deleteStepData(stepData: StepData)

    suspend fun updateStepData(stepData: StepData)

    suspend fun getHistoricalSteps(todayDate: String): List<StepData>

}