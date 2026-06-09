package com.example.healthyhabittracker.data

import android.content.Context

interface AppContainer{
    val waterReminderRepository: WaterReminderRepository
    val stepDataRepository: StepDataRepository
}

class AppDataContainer(private val context: Context): AppContainer{
    override val waterReminderRepository: WaterReminderRepository by lazy {
        OfflineWaterReminderRepository(AppDatabase.getDatabase(context).waterReminderDao())
    }
    override val stepDataRepository: StepDataRepository by lazy {
        OfflineStepDataRepository(AppDatabase.getDatabase(context).stepDataDao())
    }
}