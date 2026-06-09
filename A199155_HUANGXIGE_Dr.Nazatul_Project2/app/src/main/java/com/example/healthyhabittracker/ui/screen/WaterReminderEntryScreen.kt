package com.example.healthyhabittracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.healthyhabittracker.data.DataSource
import com.example.healthyhabittracker.ui.components.FoldCard
import com.example.healthyhabittracker.ui.components.SettingsItem
import com.example.healthyhabittracker.ui.components.SettingsMultipleChoiceSection
import com.example.healthyhabittracker.ui.components.WheelPicker
import com.example.healthyhabittracker.ui.view.WaterReminder.WaterReminderDetails
import com.example.healthyhabittracker.ui.view.WaterReminder.WaterReminderUiState
import kotlin.collections.plus


@Composable
fun WaterReminderEntryScreen(
    waterReminderUiState: WaterReminderUiState,
    onReminderValueChange: (WaterReminderDetails) -> Unit,
    onConfirmClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WaterReminderInputForm(
                waterReminderDetails = waterReminderUiState.waterReminderDetails,
                onValueChange = onReminderValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // 给按钮和表单留点间距

        OutlinedButton(
            onClick = { onConfirmClicked() },
            enabled = waterReminderUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Add")
        }
    }
}

@Composable
fun WaterReminderInputForm(
    waterReminderDetails: WaterReminderDetails,
    modifier: Modifier = Modifier,
    onValueChange: (WaterReminderDetails) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    val filteredList by remember {
        derivedStateOf {
            if (searchText.isEmpty()) {
                DataSource.waterQuotesEn
            } else {
                DataSource.waterQuotesEn.filter { it.contains(searchText, ignoreCase = true) }
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
            FoldCard(
                title = "Time: ${waterReminderDetails.hour.padStart(2, '0')}:${waterReminderDetails.minute.padStart(2, '0')}",
                expandedDefault = false
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WheelPicker(
                        items = (0..23).toList(),
                        modifier = Modifier.width(70.dp)
                    ) { hour ->
                        onValueChange(waterReminderDetails.copy(hour = hour.toString()))
                    }
                    Text(" : ", style = MaterialTheme.typography.headlineMedium)
                    WheelPicker(
                        items = (0..50 step 10).toList(),
                        modifier = Modifier.width(70.dp)
                    ) { min ->
                        onValueChange(waterReminderDetails.copy(minute = min.toString()))
                    }
                }
            }

            FoldCard(title = "Repeat Days") {
                SettingsMultipleChoiceSection(
                    title = "",
                    options = DataSource.weekDaysName,
                    selectedIndices = waterReminderDetails.repeatDays,
                    onOptionToggled = { index ->
                        val newList = if (waterReminderDetails.repeatDays.contains(index)) {
                            waterReminderDetails.repeatDays - index
                        } else {
                            waterReminderDetails.repeatDays + index
                        }
                        onValueChange(waterReminderDetails.copy(repeatDays = newList))
                    }
                )
            }

            Column {
                Text(
                    text = "Reminder Message",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search keywords...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Surface(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .heightIn(max = 200.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(filteredList) { quote ->
                            val isSelected = waterReminderDetails.label == quote
                            Text(
                                text = quote,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onValueChange(waterReminderDetails.copy(label = quote)) }
                                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                    .padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            SettingsMultipleChoiceSection(
                title = "Notification Type",
                options = DataSource.NotificationType.entries.map { it.name },
                selectedIndices = waterReminderDetails.notificationType.map { it.ordinal }
            ) { index ->
                val selectedType = DataSource.NotificationType.entries[index]
                val newList = if (waterReminderDetails.notificationType.contains(selectedType)) {
                    waterReminderDetails.notificationType - selectedType
                } else {
                    waterReminderDetails.notificationType + selectedType
                }
                onValueChange(waterReminderDetails.copy(notificationType = newList))
            }
        }
    }
}