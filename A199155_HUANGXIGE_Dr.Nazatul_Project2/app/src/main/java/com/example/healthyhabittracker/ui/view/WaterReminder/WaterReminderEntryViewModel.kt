package com.example.healthyhabittracker.ui.view.WaterReminder

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.healthyhabittracker.Notification.WaterAlarmScheduler
import com.example.healthyhabittracker.data.DataSource
import com.example.healthyhabittracker.data.WaterReminder
import com.example.healthyhabittracker.data.WaterReminderRepository

class WaterReminderEntryViewModel(private val waterReminderRepository: WaterReminderRepository) : ViewModel() {

    var waterReminderUiState by mutableStateOf(WaterReminderUiState())
        private set

    fun updateUiState(waterReminderDetails: WaterReminderDetails) {
        waterReminderUiState =
            WaterReminderUiState(
                waterReminderDetails = waterReminderDetails,
                isEntryValid = validateInput(waterReminderDetails)
            )
    }

    private fun validateInput(uiState: WaterReminderDetails = waterReminderUiState.waterReminderDetails): Boolean {
        return with(uiState) {
            hour.isNotBlank() && minute.isNotBlank() && repeatDays.isNotEmpty() && notificationType.isNotEmpty()
        }
    }

    suspend fun saveWaterReminder(context: Context) {
        if (validateInput()) {
            val reminder = waterReminderUiState.waterReminderDetails.toWaterReminder()
            waterReminderRepository.insertWaterReminder(reminder)
            val scheduler = WaterAlarmScheduler(context)
            scheduler.schedule(reminder)
        }
    }

}

data class WaterReminderUiState(
    val waterReminderDetails: WaterReminderDetails = WaterReminderDetails(),
    val isEntryValid: Boolean = false
)

data class WaterReminderDetails(
    val id: Int = 0,
    val hour: String = "",
    val minute: String = "",
    val repeatDays: List<Int> = emptyList(),
    val notificationType: List<DataSource.NotificationType> = emptyList(),
    val label: String = ""
)

fun WaterReminderDetails.toWaterReminder(): WaterReminder = WaterReminder(
    id = id,
    hour = hour,
    minute = minute,
    repeatDays = repeatDays,
    notificationType = notificationType,
    label = label
)

fun WaterReminder.toWaterReminderUiState(isEntryValid: Boolean = false): WaterReminderUiState =
    WaterReminderUiState(
    waterReminderDetails = this.toWaterReminderDetails(),
    isEntryValid = isEntryValid
)

fun WaterReminder.toWaterReminderDetails(): WaterReminderDetails = WaterReminderDetails(
    id = id,
    hour = hour,
    minute = minute,
    repeatDays = repeatDays,
    notificationType = notificationType,
    label = label
)
