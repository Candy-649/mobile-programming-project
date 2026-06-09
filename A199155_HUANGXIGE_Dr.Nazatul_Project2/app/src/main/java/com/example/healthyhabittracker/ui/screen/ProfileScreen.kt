package com.example.healthyhabittracker.ui.screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.healthyhabittracker.R
import com.example.healthyhabittracker.ui.components.ClickableInfoCard

@Composable
fun ProfileScreen(
    nickname: String,
    avatar: Uri,
    onAvatarSelected: (Uri) -> Unit,
    onNicknameClick: () -> Unit,
){
    LaunchedEffect(avatar) {
        Log.d("AVATAR_DEBUG", "Avatar: $avatar")
    }
    val launcher = rememberLauncherForActivityResult<String, Uri?>(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAvatarSelected(it) }

    }
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = "Avatar",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .clickable(onClick = { launcher.launch("image/*") })
            )
        }
        ClickableInfoCard(
            title = "Nickname",
            value = nickname,
            onClick = onNicknameClick
        )
    }
}

