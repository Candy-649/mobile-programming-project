package com.example.healthyhabittracker.ui.view

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.example.healthyhabittracker.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val username: String = "Username",
    val avatarUrl: String = "android.resource://com.example.healthyhabittracker/${R.drawable.photo_default}",
    val isSignUpMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    var uiState by mutableStateOf(AuthUiState())
        private set


    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password, errorMessage = null)
    }

    fun updateConfirmPassword(password: String) {
        uiState = uiState.copy(confirmPassword = password, errorMessage = null)
    }


    fun toggleMode() {
        uiState = uiState.copy(
            isSignUpMode = !uiState.isSignUpMode,
            errorMessage = null,
            password = "",
            confirmPassword = ""
        )
    }

    fun authenticate(onSuccess: () -> Unit) {
        if (!validateForm()) return

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                if (uiState.isSignUpMode) {
                    val result = auth.createUserWithEmailAndPassword(
                        uiState.email.trim(),
                        uiState.password
                    ).await()


                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(uiState.username)
                        .setPhotoUri(Uri.parse(uiState.avatarUrl))
                        .build()

                    result.user?.updateProfile(profileUpdates)?.await()

                } else {
                    auth.signInWithEmailAndPassword(uiState.email.trim(), uiState.password).await()
                }

                uiState = uiState.copy(isLoading = false)
                onSuccess()

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Authentication failed"
                )
            }
        }
    }

    private fun validateForm(): Boolean {
        val email = uiState.email.trim()
        val password = uiState.password
        val confirmPassword = uiState.confirmPassword

        if (email.isBlank()) {
            uiState = uiState.copy(errorMessage = "Email can't be empty.")
            return false
        }
        if (password.length < 6) {
            uiState = uiState.copy(errorMessage = "Password should at least contains 6 characters")
            return false
        }
        if (uiState.isSignUpMode && password != confirmPassword) {
            uiState = uiState.copy(errorMessage = "Passwords don't match")
            return false
        }
        return true
    }

    fun signOut() {
        auth.signOut()
    }
}