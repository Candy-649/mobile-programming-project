package com.example.healthyhabittracker.ui.screen

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthyhabittracker.ui.components.SettingsItem
import com.example.healthyhabittracker.ui.components.SettingsSection
import com.example.healthyhabittracker.ui.components.SettingsSwitchItem
import com.example.healthyhabittracker.ui.theme.HealthyHabitTrackerTheme

@Composable
fun SettingsScreen(
    notificationsOn: Boolean,
    darkMode: Boolean,
    darkModeFollowSystem: Boolean,
    onEditProfile: () -> Unit = {},
    onNotificationToggle: (Boolean) -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit = {},
    onFollowSystemToggle: (Boolean) -> Unit = {},
    onNotificationPermissionClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val spacing = 12.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        SettingsSection(title = "Account") {
            SettingsItem(text = "Edit Profile", onClick = onEditProfile)
        }

        SettingsSection(title = "Notifications") {
            SettingsSwitchItem(text = "Notifications", checked = notificationsOn, onCheckedChange = onNotificationToggle)
            SettingsItem(
                text = "Notification Permission",
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply{
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                        }
                    else {
                        Toast.makeText(context, "No need to set permission manually", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        SettingsSection(title = "Appearance") {
            SettingsSwitchItem(text = "Dark Mode", checked = darkMode, onCheckedChange = onDarkModeToggle)
            SettingsSwitchItem(text = "Follow System", checked = darkModeFollowSystem, onCheckedChange = onFollowSystemToggle)
        }

        SettingsSection(title = "Permissions") {
            SettingsItem(text = "Notification Permission", onClick = onNotificationPermissionClick)
        }
    }
}



@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsNightPreview() {
    HealthyHabitTrackerTheme {
        SettingsScreen(
            notificationsOn = true,
            darkMode = false,
            darkModeFollowSystem = true
        ) {  }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SettingsLightPreview() {
    HealthyHabitTrackerTheme {
        SettingsScreen(
            notificationsOn = true,
            darkMode = false,
            darkModeFollowSystem = true
        ) {  }
    }
}
