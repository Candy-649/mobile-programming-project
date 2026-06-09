package com.example.healthyhabittracker.ui.view.WaterReminder

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyhabittracker.Notification.WaterAlarmScheduler
import com.example.healthyhabittracker.data.WaterReminder
import com.example.healthyhabittracker.data.WaterReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class WaterReminderEditViewModel(
    private val waterReminderRepository: WaterReminderRepository
) : ViewModel() {

    val reminderList: StateFlow<List<WaterReminder>> = waterReminderRepository.getAllWaterReminders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var uiState by mutableStateOf(WaterReminderUiState())
        private set

    fun setUiState(details: WaterReminderDetails){
        uiState = WaterReminderUiState(
            waterReminderDetails = details,
            isEntryValid = false
        )
    }

    fun updateUiState(details: WaterReminderDetails){
        uiState = WaterReminderUiState(
            waterReminderDetails = details,
            isEntryValid = validateInput(details)
        )
    }

    suspend fun updateReminder(context: Context){
        if (validateInput(uiState.waterReminderDetails)){
            val updatedReminder = uiState.waterReminderDetails.toWaterReminder()
            waterReminderRepository.updateWaterReminder(updatedReminder)
            val scheduler = WaterAlarmScheduler(context)
            scheduler.schedule(updatedReminder)
        }
    }

    suspend fun deleteReminder(reminder: WaterReminder, context: Context){
        waterReminderRepository.deleteWaterReminder(reminder)

        val scheduler = WaterAlarmScheduler(context)
        scheduler.cancel(reminder)
    }

    private fun validateInput(uiState: WaterReminderDetails): Boolean {
        return uiState.hour.isNotBlank() && uiState.minute.isNotBlank() && uiState.repeatDays.isNotEmpty() && uiState.notificationType.isNotEmpty() && uiState.label.isNotBlank()
    }
}