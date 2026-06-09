package com.example.healthyhabittracker.ui.components

import android.Manifest
import android.R
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class WaterReminderReceiver : android.content.BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val notificationHelper = NotificationHelper(context)
            notificationHelper.showNotification("Drink Water", "It's time to drink water!")
        }
    }
}

class NotificationHelper(private val context: Context) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, "water_channel")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val manager = NotificationManagerCompat.from(context)
        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

object NotificationUtils {
    fun createNotificationChannel(context: Context) {
        val name = "WaterReminder"
        val descriptionText = "Water Reminder"
        val importance = android.app.NotificationManager.IMPORTANCE_HIGH
        val channel = android.app.NotificationChannel("water_channel", name, importance)
        channel.description = descriptionText
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}