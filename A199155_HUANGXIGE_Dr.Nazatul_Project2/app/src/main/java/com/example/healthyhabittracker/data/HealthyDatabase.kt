package com.example.healthyhabittracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WaterReminder::class, StepData::class], version = 2, exportSchema = false)
@TypeConverters(NotificationTypeConverter::class, RepeatDaysConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterReminderDao(): WaterReminderDao
    abstract fun stepDataDao(): StepDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}