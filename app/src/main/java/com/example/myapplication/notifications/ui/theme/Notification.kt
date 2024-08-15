package com.example.myapplication.notifications.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.example.myapplication.Sharedpreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.annotations.SerializedName
import android.util.Log
import com.example.yourapp.ui.Request
import com.example.yourapp.ui.RequestStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.Duration
import java.time.LocalDate
import java.util.Locale

// Data model to hold Notification data
data class NotificationData(
    val id: String,
    val backgroundColor: Color,
    val stripeColor: Color,
    val icon: Int,
    val text: String,
    val time: String
)

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

// Helper function to display time ago format
@RequiresApi(Build.VERSION_CODES.S)
fun timeAgo(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
    val requestTime = Instant.from(formatter.parse(timestamp))
    val now = Instant.now()
    val duration = Duration.between(requestTime, now)
    val seconds = duration.toSeconds()

    return when {
        seconds < 60 -> "$seconds sec ago"
        seconds < 3600 -> "${seconds / 60}m ago"
        seconds < 86400 -> "${seconds / 3600} hours ago"
        seconds < 604800 -> "${seconds / 86400} days ago"
        seconds < 2419200 -> "${seconds / 604800} weeks ago"
        else -> "${seconds / 2419200} months ago"
    }
}

// Function to fetch notifications from the backend
private fun fetchNotifications(userId: String, onResult: (List<NotificationData>) -> Unit) {
    RetrofitClient.apiService.getRequests(userId).enqueue(object : Callback<List<Request>> {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let { requests ->
                    val notifications = requests
                        .filter { it.status != RequestStatus.PENDING } // Exclude pending requests
                        .map { request ->
                            NotificationData(
                                id = request.id,
                                backgroundColor = if (request.status == RequestStatus.APPROVED) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                stripeColor = if (request.status == RequestStatus.APPROVED) Color(0xFF19C588) else Color(0xFFF44336),
                                icon = if (request.status == RequestStatus.APPROVED) R.drawable.icon_check else R.drawable.icon_deny,
                                text = "Your request to swap from the ${formatDate(request.changeDayFrom)} to the ${formatDate(request.changeDayTo)} was ${request.status.name.lowercase()}.",
                                time = request.time
                            )
                        }.sortedByDescending { it.time } // Sort by latest first
                    onResult(notifications)
                }
            } else {
                Log.e("fetchNotifications", "Failed to fetch notifications: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Request>>, t: Throwable) {
            Log.e("fetchNotifications", "Error fetching notifications", t)
        }
    })
}

// Function to fetch notifications from the backend
private fun fetchAllNotifications(onResult: (List<NotificationData>) -> Unit) {
    RetrofitClient.apiService.getAllRequests().enqueue(object : Callback<List<Request>> {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let { requests ->
                    val notifications = requests
                        .filter { it.status != RequestStatus.PENDING } // Exclude pending requests
                        .map { request ->
                            NotificationData(
                                id = request.id,
                                backgroundColor = if (request.status == RequestStatus.APPROVED) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                stripeColor = if (request.status == RequestStatus.APPROVED) Color(0xFF19C588) else Color(0xFFF44336),
                                icon = if (request.status == RequestStatus.APPROVED) R.drawable.icon_check else R.drawable.icon_deny,
                                text = "You have ${request.status.name.lowercase()} ${request.username}'s request to swap from the ${formatDate(request.changeDayFrom)} to the ${formatDate(request.changeDayTo)}.",
                                time = request.time
                            )
                        }.sortedByDescending { it.time } // Sort by latest first
                    onResult(notifications)
                }
            } else {
                Log.e("fetchNotifications", "Failed to fetch notifications: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Request>>, t: Throwable) {
            Log.e("fetchNotifications", "Error fetching notifications", t)
        }
    })
}



// Main composable function to render Notification Screen
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
@Composable
fun NotificationCard(
    backgroundColor: Color,
    stripeColor: Color,
    icon: Int,
    text: String
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(stripeColor)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(9.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

