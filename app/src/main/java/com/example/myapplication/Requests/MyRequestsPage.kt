package com.example.yourapp.ui

import SharedViewModel
import android.content.Context
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.annotation.RequiresApi
import java.time.Duration

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

import java.time.ZoneId

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

import java.time.temporal.ChronoUnit
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.S)
fun formatDate(dateString: String): String {
    // Define possible date formats
    val possibleDateFormats = listOf(
        "yyyy-MM-dd", // Expected format
        "dd-MM-yyyy"  // Alternate format
    )

    for (format in possibleDateFormats) {
        try {
            // Parse the date from the format
            val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
            val date = LocalDate.parse(dateString, formatter)

            // Extract the day, month, and year
            val day = date.dayOfMonth
            val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }

            // Determine the correct suffix for the day
            val suffix = when (day) {
                1, 21, 31 -> "st"
                2, 22 -> "nd"
                3, 23 -> "rd"
                else -> "th"
            }

            // Return the formatted date as "21st of August"
            return "$day$suffix of $month"
        } catch (e: Exception) {
            // If parsing fails, continue to the next format
        }
    }

    // If none of the formats work, return the original date string
    return dateString
}


@RequiresApi(Build.VERSION_CODES.S)
fun timeAgo(timestamp: String): String {

    // Assuming timestamp is in "yyyy-MM-dd HH:mm:ss" format
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


data class Request(
    @SerializedName("requestID") val id: String = "",
    @SerializedName("timeStamp") val time: String = "",
    @SerializedName("changeDayFrom") val changeDayFrom: String = "",
    @SerializedName("changeDayTo") val changeDayTo: String = "",
    @SerializedName("Status") var status: RequestStatus = RequestStatus.PENDING,
    @SerializedName("username") val username: String = ""

)

enum class RequestStatus {
    @SerializedName("Pending") PENDING,
    @SerializedName("Approved") APPROVED,
    @SerializedName("Denied") DENIED
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MyRequestsPage() {
    val context = LocalContext.current // Get the current Context
    val userId = Sharedpreference.getUserId(context) ?: return // Return early if userId is null
    var requestList by remember { mutableStateOf(listOf<Request>()) }

    LaunchedEffect(Unit) {
        fetchRequests(userId) { fetchedRequests ->
            Log.d("MyRequestsPage", "Fetched requests: $fetchedRequests")
            requestList = fetchedRequests
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .background(Color.Transparent),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "My Requests",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .padding(top = 10.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(requestList) { request ->
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .alpha(0.5f)
                        )
                        RequestItem(
                            request = request,
                            onCancelRequest = { requestToCancel ->
                                deleteRequest(requestToCancel.id) {
                                    requestList = requestList.filter { it.id != requestToCancel.id }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}


private fun fetchRequests(userId: String, onResult: (List<Request>) -> Unit) {
    RetrofitClient.apiService.getRequests(userId).enqueue(object : Callback<List<Request>> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let { requests ->
                    val sortedRequests = requests.sortedByDescending { request ->
                        // Check if the timestamp is not empty or null
                        if (request.time.isNotEmpty()) {
                            try {
                                // Parse the timestamp string into a LocalDateTime object
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                val dateTime = LocalDateTime.parse(request.time, formatter)

                                // Convert LocalDateTime to Instant using system default time zone
                                dateTime.atZone(ZoneId.systemDefault()).toInstant()
                            } catch (e: DateTimeParseException) {
                                Log.e("fetchRequests", "Failed to parse timestamp: ${request.time}")
                                Instant.MIN // Fallback for sorting purposes if parsing fails
                            }
                        } else {
                            Log.e("fetchRequests", "Empty timestamp for request ID: ${request.id}")
                            Instant.MIN // Fallback for sorting purposes if time is empty
                        }
                    }
                    onResult(sortedRequests)
                }
            } else {
                Log.e("fetchRequests", "Failed to fetch requests: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Request>>, t: Throwable) {
            Log.e("fetchRequests", "Error fetching requests", t)
        }
    })
}






private fun deleteRequest(requestId: String, onResult: () -> Unit) {
    RetrofitClient.apiService.deleteRequest(requestId).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                onResult()
            } else {
                Log.e("deleteRequest", "Failed to delete request: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.e("deleteRequest", "Error deleting request", t)
        }
    })
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RequestItem(request: Request, onCancelRequest: (Request) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = timeAgo(request.time), // Use the timeAgo function here
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Request to swap your scheduled work locations between the ${formatDate(request.changeDayFrom)} & the ${formatDate(request.changeDayTo)}",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))
        when (request.status) {
            RequestStatus.PENDING -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = "Cancel Request",
                        tint = Color(0xFF76B31B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Cancel Request",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF76B31B),
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            onCancelRequest(request)
                        }
                    )
                }
            }
            RequestStatus.APPROVED -> {
                StatusBox(text = "Approved", backgroundColor = Color(0xFF19C588))
            }
            RequestStatus.DENIED -> {
                StatusBox(text = "Denied", backgroundColor = Color(0xFFFEB5757))
            }
        }
    }
}



@Composable
fun StatusBox(text: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 18.sp)
    }
}
