package com.example.myapplication.NotificationSreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Sharedpreference
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Data model to hold Notification data

// Helper function to format the date as "21st of August"
@RequiresApi(Build.VERSION_CODES.S)
fun formatDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val date = LocalDate.parse(dateString, formatter)
    val day = date.dayOfMonth
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val suffix = when (day) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
    return "$day$suffix of $month"
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NotificationScreen(isManager: Boolean) {

    val context = LocalContext.current
    val userId = Sharedpreference.getUserId(context) ?: return
    var notificationItems by remember { mutableStateOf(listOf<NotificationData>()) }



    if (isManager) {

        LaunchedEffect(Unit) {
            fetchAllNotifications() { fetchedNotifications ->
                notificationItems = fetchedNotifications
            }
        }

    } else{

        LaunchedEffect(Unit) {
            fetchNotifications(userId) { fetchedNotifications ->
                notificationItems = fetchedNotifications
            }
        }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notificationItems) { notification ->
                        NotificationCard(
                            backgroundColor = notification.backgroundColor,
                            stripeColor = notification.stripeColor,
                            icon = notification.icon,
                            text = notification.text
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

// Composable to render individual Notification Card


