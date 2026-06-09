package com.example.healthyhabittracker.ui.view.StepData

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyhabittracker.data.FirebaseStepData
import com.example.healthyhabittracker.data.StepData
import com.example.healthyhabittracker.data.StepDataDao
import com.example.healthyhabittracker.data.StepDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class StepDataEntryViewModel(private val repository: StepDataRepository): ViewModel(){
    var stepData: StateFlow<StepData> = repository.getStepData(LocalDate.now().toString())
        .map { it ?: StepData(LocalDate.now().toString()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StepData(date = LocalDate.now().toString())
        )

    val stepProgress: StateFlow<Float> = stepData
        .map { data ->
            if (data.goal > 0){
                (data.steps.toFloat() / data.goal.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5),
            initialValue = 0f
        )

    var tempGoal by mutableStateOf(0)
        private set

    fun setTempGoal(stepData: StepData){
        tempGoal = stepData.goal
    }

    fun saveGoal(newGoal: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val updateData = stepData.value.copy(goal = newGoal)
            repository.insertStepData(updateData)
        }
    }


    init {
        initializeStepData()
    }

    private fun initializeStepData(){
        viewModelScope.launch {
            val initialStepData = repository.getStepData(LocalDate.now().toString()).firstOrNull()
            if (initialStepData == null) {
                val lastRecord = repository.getLastStepData()
                if (lastRecord != null){
                    val newRecord = StepData(
                        date = LocalDate.now().toString(),
                        goal = lastRecord.goal
                    )
                    repository.insertStepData(newRecord)
                }
            }
        }
    }

    fun syncHistoricalDataToFireBase(){
        val today = LocalDate.now().toString()
        val firestore = com.google.firebase.ktx.Firebase.firestore
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance()
        val userId = currentUser.uid ?: ""
        viewModelScope.launch {
            val historicalList = repository.getHistoricalSteps(today)

            if (historicalList.isNotEmpty()){
                historicalList.forEach { entity ->
                    val firebaseData = FirebaseStepData(
                        userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                        date = entity.date,
                        steps = entity.steps,
                        goal = entity.goal,
                        timestamp = System.currentTimeMillis()
                    )

                    firestore.collection("users")
                        .document(userId)
                        .collection("steps")
                        .document(entity.date)
                        .set(firebaseData)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Historical data synced successfully")
                        }
                }
            }
        }
    }

}

data class StepDataDetails(
    val date: String = "",
    val steps: Int = 0,
    val goal: Int = 0
)

data class StepDataUiState(
    val stepDataDetails: StepDataDetails = StepDataDetails(),
    val oldGoal: Int = stepDataDetails.goal
){
    val isChanged: Boolean = stepDataDetails.goal != oldGoal
}
