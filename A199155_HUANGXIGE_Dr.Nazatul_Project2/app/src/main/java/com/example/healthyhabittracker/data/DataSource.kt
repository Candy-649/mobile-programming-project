package com.example.healthyhabittracker.data

import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DataSource {
    val waterIntake = (1000..3000 step 100).toList()

    val stepCount = listOf(
        5000,
        6000,
        7000,
        8000,
        9000,
        10000
    )

    val timeSlots = listOf("08:00", "10:00", "12:00", "15:00", "18:00", "20:00")

    val yesOrNoOptions = listOf("Yes", "No")

    @Serializable
    enum class NotificationType {
        SOUND,
        VIBRATE,
        POPUP
    }

    val weekDays = (1..7).toList()

    val weekDaysName = weekDays.map { weekDays ->
        when (weekDays) {
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            7 -> "Sunday"
            else -> ""
        }
    }

    val waterQuotesEn = listOf(
        "Stay hydrated, stay healthy! 💧",
        "Time for a water break.",
        "Drink water, feel better.",
        "Your body is 60% water, keep it up!",
        "H2O is the way to go.",
        "Fuel your brain with a glass of water.",
        "Keep calm and drink water.",
        "Every sip counts towards your goal.",
        "Water: your body's natural fuel.",
        "Glowing skin starts with hydration. ✨",
        "Don't wait until you're thirsty!",
        "Flush out those toxins, keep drinking.",
        "Think clear, drink clear.",
        "Water is your best workout partner.",
        "Boost your energy with a quick sip.",
        "Hydration is the key to productivity.",
        "Drink more water, stress less.",
        "Your muscles need water to grow. 💪",
        "A glass of water a day keeps the fatigue away.",
        "Daily goal: Stay hydrated and awesome!"
    )
}

