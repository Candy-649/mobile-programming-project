package com.example.healthyhabittracker.ui.view.Profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.healthyhabittracker.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ProfileViewModel : ViewModel(){
    private val auth = Firebase.auth

    var nickname by mutableStateOf(auth.currentUser?.displayName ?: "Username")
        private set

    var avatarUri by mutableStateOf(
        auth.currentUser?.photoUrl?.toString()
            ?: "android.resource://com.example.healthyhabittracker/${R.drawable.photo_default}"
    )
        private set

    var mobileNumber by mutableStateOf(
        auth.currentUser?.phoneNumber ?: "You haven't set your number"
    )
        private set

    fun refreshProfile() {
        val user = auth.currentUser
        nickname = user?.displayName ?: "Username"
        avatarUri = user?.photoUrl?.toString() ?: "android.resource://com.example.healthyhabittracker/drawable/photo_default"
        mobileNumber = user?.phoneNumber ?: "Not set"
    }
}