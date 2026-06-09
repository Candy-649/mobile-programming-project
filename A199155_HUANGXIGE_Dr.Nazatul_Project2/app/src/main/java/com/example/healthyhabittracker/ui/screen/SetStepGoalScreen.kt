package com.example.healthyhabittracker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthyhabittracker.R
import com.example.healthyhabittracker.data.DataSource
import com.example.healthyhabittracker.ui.components.BottomSheet
import com.example.healthyhabittracker.ui.components.ClickableInfoCard
import com.example.healthyhabittracker.ui.components.FoldCard
import com.example.healthyhabittracker.ui.components.WheelPicker
import com.example.healthyhabittracker.ui.theme.HealthyHabitTrackerTheme
import com.example.healthyhabittracker.ui.view.StepData.StepDataDetails
import java.time.LocalDate

@Composable
fun SetStepGoalScreen(
    title: String,
    options: List<Int>,
    unit: String,
    tempGoal: Int,
    onCancelClicked: () -> Unit,
    onConfirmClicked: (Int) -> Unit,
){
    var showYearSheet by remember { mutableStateOf(false) }
    var year by remember { mutableIntStateOf(LocalDate.now().year) }
    var age = 0
    var isSedentary by remember { mutableStateOf("") }
    var isShaping by remember { mutableStateOf("") }
    var step by remember { mutableIntStateOf(tempGoal) }
    val yesOrNo = DataSource.yesOrNoOptions
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        FoldCard(
            title = "I already know my goal"
        ) {
            WheelPicker(
                items = options
            ) { step = it
            }
        }
        FoldCard(
            title = "I'm not sure about my goal"
        ) {
            Column {
                ClickableInfoCard(
                    title = "Please enter your year of birth: ",
                    value = "$year",
                    onClick = { showYearSheet = true }
                )
                if (showYearSheet) {
                    BottomSheet(
                        title = "Year:",
                        initialValue = 2025,
                        content = {
                            WheelPicker(
                                items = (1900..LocalDate.now().year).toList().reversed(),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            ) { value ->
                                year = value
                            }
                        }
                    ) {
                        age = LocalDate.now().year - year
                        showYearSheet = false
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Are you Sedentary?",
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                            .fillMaxWidth()
                    ) {
                        yesOrNo.forEach { item ->
                            SegmentedButton(
                                selected = item == isSedentary,
                                onClick = { isSedentary = item },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = yesOrNo.indexOf(
                                        item
                                    ), count = yesOrNo.size
                                )
                            ) {
                                Text(item)
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Are you Shaping?",
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                            .fillMaxWidth()
                    ) {
                        yesOrNo.forEach { item ->
                            SegmentedButton(
                                selected = item == isShaping,
                                onClick = { isShaping = item },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = yesOrNo.indexOf(
                                        item
                                    ), count = yesOrNo.size
                                )
                            ) {
                                Text(item)
                            }
                        }
                    }
                }

                if (isShaping != "" && isSedentary != "" && year != LocalDate.now().year) run {
                    val age = age
                    val sedentary = if (isSedentary == "No") 0 else 1
                    val shaping = if (isShaping == "No") 0 else 1
                    step = when {
                        age >= 20 -> 6000
                        age in 16..19 -> 7000
                        else -> 5000
                    }


                    if (sedentary == 1) step += 1000
                    if (shaping == 1) step += 1000
                }
            }
        }
        Text(
            text = "Goal: $step $unit",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .fillMaxWidth(),
            textAlign = TextAlign.Right
        )
        Spacer(modifier = Modifier
            .weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancelClicked) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledTonalButton(onClick = {onConfirmClicked(step)}, enabled = step != tempGoal) {
                Text("Confirm")
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SetStepGoalNightPreview() {
    HealthyHabitTrackerTheme {
        SetStepGoalScreen(
            title = "Set Step Goal",
            options = DataSource.stepCount,
            unit = "steps",
            tempGoal = 8000,
            onCancelClicked = {},
            onConfirmClicked = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SetStepGoalLightPreview() {
    HealthyHabitTrackerTheme {
        SetStepGoalScreen(
            title = "Set Water Goal",
            options = DataSource.stepCount,
            unit = "steps",
            tempGoal = 8000,
            onCancelClicked = {},
            onConfirmClicked = {}
        )
    }
}

