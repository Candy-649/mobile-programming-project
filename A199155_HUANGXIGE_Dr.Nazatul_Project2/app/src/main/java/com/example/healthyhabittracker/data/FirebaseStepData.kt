package com.example.healthyhabittracker.data

data class FirebaseStepData(
    val userId: String = "",
    val date: String = "",
    val steps: Int = 0,
    val goal: Int = 0,
    val timestamp: Long = 0
)
