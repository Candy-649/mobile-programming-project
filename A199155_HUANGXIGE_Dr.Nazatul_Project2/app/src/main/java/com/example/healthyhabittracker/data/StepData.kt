package com.example.healthyhabittracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_count_table")
data class StepData(
    @PrimaryKey
    val date: String,

    @ColumnInfo(name = "step_count")
    var steps: Int = 0,

    @ColumnInfo(name = "goal_steps")
    var goal: Int = 8000
)
