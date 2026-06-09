package com.example.healthyhabittracker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthyhabittracker.R
import com.example.healthyhabittracker.data.WaterReminder
import com.example.healthyhabittracker.ui.components.CircularProgress
import com.example.healthyhabittracker.ui.components.FoldCard
import com.example.healthyhabittracker.ui.components.SettingsItem
import com.example.healthyhabittracker.ui.components.SwipeActionWrapper
import com.example.healthyhabittracker.ui.view.WaterReminder.WaterReminderDetails
import com.example.healthyhabittracker.ui.view.WaterReminder.toWaterReminderDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    timeText: String,
    dateText: String,
    waterReminderList: List<WaterReminder>,
    stepProgress: Float,
    stepLevel: String,
    stepGoal: Int,
    editingDetails: WaterReminderDetails,
    onReminderClicked: () -> Unit,
    onEditClicked: (WaterReminderDetails) -> Unit,
    onValueChange: (WaterReminderDetails) -> Unit,
    onConfirmClicked: () -> Unit,
    onDelete: (WaterReminder) -> Unit
) {
    var selectedId by remember { mutableStateOf(0) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "  $dateText",
                    modifier = Modifier.padding(start = 2.dp, top = 2.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            FoldCard(
                title = stringResource(R.string.step_home_label),
                expandedDefault = true
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgress(
                        progress = stepProgress,
                        color = MaterialTheme.colorScheme.tertiary,
                        strokeWidth = 12.dp,
                        text = stepLevel,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
                    Text(
                        text = "Step Goal: $stepGoal",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        items(
            items = waterReminderList,
            key = { it.id }
        ) { reminder ->
            SwipeActionWrapper(
                item = reminder,
                onDelete = { onDelete(reminder) },
                cornerRadius = 12.dp
            ) {
                SettingsItem(
                    text = "${reminder.hour}:${reminder.minute}"
                ) {
                    onEditClicked(reminder.toWaterReminderDetails())
                    showSheet = true
                }
            }
        }

        item {
            OutlinedButton(
                onClick = onReminderClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Set Water Reminder")
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {showSheet = false},
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WaterReminderInputForm(
                        waterReminderDetails = editingDetails,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = onValueChange
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onConfirmClicked()
                        showSheet = false
                    }
                ) {
                    Text("Update")
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        timeText = "10:00",
        dateText = "2025-11-20",
        waterReminderList = listOf(),
        stepProgress = 0.5f,
        stepLevel = "3000",
        stepGoal = 8000,
        editingDetails = WaterReminderDetails(),
        onReminderClicked = {},
        onEditClicked = {},
        onValueChange = {},
        onConfirmClicked = {},
        onDelete = {}
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenDarkThemePreview(){
    HomeScreen(
        timeText = "10:00",
        dateText = "2025-11-20",
        waterReminderList = listOf(),
        stepProgress = 0.5f,
        stepLevel = "3000",
        stepGoal = 8000,
        editingDetails = WaterReminderDetails(),
        onReminderClicked = {},
        onEditClicked = {},
        onValueChange = {},
        onConfirmClicked = {},
        onDelete = {}
    )
}