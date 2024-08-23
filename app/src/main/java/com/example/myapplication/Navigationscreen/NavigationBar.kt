package com.example.myapplication.Navigationscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.Screens


@Composable
fun CustomBottomNavigationBar(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    additionalIcon1: (@Composable () -> Unit)? = null, // Add this parameter
    additionalIcon2: (@Composable () -> Unit)? = null, // Add this parameter
    showNotificationIcon: Boolean = true // New parameter to control visibility
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFECECEC))
            .height(90.dp)
            .clip(RoundedCornerShape(32.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home button
            BarIcon(
                selected = selectedScreen == Screens.Home.screen,
                iconId = if (selectedScreen == Screens.Home.screen) R.drawable.calendergreen else R.drawable.calendergray,
                contentDescription = "Home",
                onClick = {
                    if (selectedScreen != Screens.Home.screen) {
                        onScreenSelected(Screens.Home.screen)
                    }
                }
            )
            // Chatbot button
            BarIcon(
                selected = selectedScreen == Screens.Chatbot.screen,
                iconId = if (selectedScreen == Screens.Chatbot.screen) R.drawable.chatggreen else R.drawable.chatgray,
                contentDescription = "Chatbot",
                onClick = {
                    if (selectedScreen != Screens.Chatbot.screen) {
                        onScreenSelected(Screens.Chatbot.screen)
                    }
                }
            )
            additionalIcon1?.invoke()

            // Requests button
            BarIcon(
                selected = selectedScreen == Screens.Requests.screen,
                iconId = if (selectedScreen == Screens.Requests.screen) R.drawable.requestgreen else R.drawable.requestgray,
                contentDescription = "Requests",
                onClick = {
                    if (selectedScreen != Screens.Requests.screen) {
                        onScreenSelected(Screens.Requests.screen)
                    }
                }
            )

            // Conditionally show the Notifications button
            if (showNotificationIcon) {
                BarIcon(
                    selected = selectedScreen == Screens.Notification.screen,
                    iconId = if (selectedScreen == Screens.Notification.screen) R.drawable.notifygreen else R.drawable.notifygray,
                    contentDescription = "Notifications",
                    onClick = {
                        if (selectedScreen != Screens.Notification.screen) {
                            onScreenSelected(Screens.Notification.screen)
                        }
                    }
                )
            }

            additionalIcon2?.invoke()
        }
    }
}





