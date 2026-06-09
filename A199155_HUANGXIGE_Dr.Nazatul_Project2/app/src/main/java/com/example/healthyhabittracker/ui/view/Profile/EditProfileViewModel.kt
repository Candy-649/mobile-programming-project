package com.example.healthyhabittracker.ui.view.Profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import kotlin.math.atan

class EditProfileViewModel : ViewModel(){
    private val auth = Firebase.auth
    private val currentUser get() = auth.currentUser

    var nickname by mutableStateOf(currentUser?.displayName ?: "Username")
        private set

    var avatarUri by mutableStateOf(
        currentUser?.photoUrl?.toString() ?: "android.resource://com.example.healthyhabittracker/drawable/photo_default"
    )
        private set

    fun updateNickname(newNickname: String, onCompleted: () -> Unit) {
        viewModelScope.launch {
            try {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newNickname)
                    .build()

                currentUser?.updateProfile(profileUpdates)?.await()
                nickname = newNickname
                onCompleted()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    fun updateAvatar(newUri: Uri, context: Context){
        viewModelScope.launch {
            try {
                val avatarFile = File(context.filesDir, "avatar.jpg")

                context.contentResolver.openInputStream(newUri)?.use { inputStream ->
                    avatarFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                val permanentUri = Uri.fromFile(avatarFile)
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(permanentUri)
                    .build()
                currentUser?.updateProfile(profileUpdates)?.await()
                avatarUri = newUri.toString()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}