package com.example.myapplication.notifications.ui.theme

import androidx.compose.ui.graphics.Color

data class NotificationData(
    val id: String,
    val backgroundColor: Color,
    val stripeColor: Color,
    val icon: Int,
    val text: String,
    val time: String
)
