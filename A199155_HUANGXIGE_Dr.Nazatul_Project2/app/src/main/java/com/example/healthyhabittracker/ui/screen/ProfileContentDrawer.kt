package com.example.healthyhabittracker.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.healthyhabittracker.R
import com.example.healthyhabittracker.ui.components.JumpCard


@Composable
fun ProfileContentDrawer(
    name: String,
    avatar: Uri,
    onEditClick: () -> Unit,
    onSetWaterReminder: () -> Unit,
    onSetDailyStepClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(36.dp))
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column {
            JumpCard(
                icon = Icons.Default.Opacity,
                text = stringResource(R.string.set_water_reminder),
                content = onSetWaterReminder
            )
            JumpCard(
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                text = stringResource(R.string.set_daily_step_goal),
                content = onSetDailyStepClick
            )
            JumpCard(
                icon = Icons.Outlined.Settings,
                text = "Setting",
                content = onSettingClick
            )
        }
    }
}
