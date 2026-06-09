package com.example.healthyhabittracker.data

import kotlinx.coroutines.flow.Flow

class OfflineStepDataRepository(private val StepDataDao: StepDataDao): StepDataRepository {
    override fun getAllStepData(): Flow<List<StepData>> = StepDataDao.getAllStepData()

    override fun getStepData(date: String): Flow<StepData?> = StepDataDao.getStepData(date)

    override suspend fun getLastStepData(): StepData? = StepDataDao.getLastStepData()

    override suspend fun insertStepData(stepData: StepData) = StepDataDao.insert(stepData)

    override suspend fun deleteStepData(stepData: StepData) = StepDataDao.delete(stepData)

    override suspend fun updateStepData(stepData: StepData) = StepDataDao.update(stepData)

    override suspend fun getHistoricalSteps(todayDate: String): List<StepData> = StepDataDao.getHistoricalSteps(todayDate)


}