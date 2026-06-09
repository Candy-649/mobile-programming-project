package com.example.healthyhabittracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthyhabittracker.HealthyHabitTracker
import com.example.healthyhabittracker.ui.view.StepData.StepDataEntryViewModel
import com.example.healthyhabittracker.ui.view.WaterReminder.WaterReminderEditViewModel
import com.example.healthyhabittracker.ui.view.WaterReminder.WaterReminderEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory{
        initializer {
            WaterReminderEntryViewModel(healthyHabitTrackerApplication().container.waterReminderRepository)
        }
        initializer {
            WaterReminderEditViewModel(healthyHabitTrackerApplication().container.waterReminderRepository)
        }
        initializer {
            StepDataEntryViewModel(healthyHabitTrackerApplication().container.stepDataRepository)
        }
    }
}

fun CreationExtras.healthyHabitTrackerApplication(): HealthyHabitTracker =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HealthyHabitTracker)