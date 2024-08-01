package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var showSplashScreen by mutableStateOf(true)
        private set

    var showLoginScreen by mutableStateOf(false)
        private set

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        viewModelScope.launch {
            // Simulate checking user status (e.g., authentication)
            // For example, after a delay, set the values accordingly
            // Here we just set some sample values
            showSplashScreen = false
            showLoginScreen = true // or false based on actual logic
        }
    }
}
