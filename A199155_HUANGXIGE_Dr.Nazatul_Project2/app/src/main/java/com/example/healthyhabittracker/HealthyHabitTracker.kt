package com.example.healthyhabittracker

import android.app.Application
import com.example.healthyhabittracker.data.AppContainer
import com.example.healthyhabittracker.data.AppDataContainer

class HealthyHabitTracker: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
