package com.example.yourapp.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.Requests.DeleteRequest
import com.example.myapplication.Requests.Request
import com.example.myapplication.Requests.RequestItem
import com.example.myapplication.Requests.fetchRequests
import com.example.myapplication.Sharedpreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.annotations.SerializedName
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
@Composable
fun MyRequestsPage() {
    val context = LocalContext.current // Get the current Context
    val userId = Sharedpreference.getUserId(context) ?: return // Return early if userId is null
    var requestList by remember { mutableStateOf(listOf<Request>()) }

    // State for managing the confirmation dialog
    var showDialog by remember { mutableStateOf(false) }
    var currentRequest by remember { mutableStateOf<Request?>(null) }

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
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = stringResource(id = R.string.My_Requests),
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
                                // Show confirmation dialog before canceling
                                currentRequest = requestToCancel
                                showDialog = true
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Confirmation Dialog
    if (showDialog && currentRequest != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Action") },
            text = {
                Text("Are you sure you want to cancel this request?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        currentRequest?.let {
                            deleteRequest(it.id) {
                                requestList = requestList.filter { req -> req.id != it.id }
                            }
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76B31B))
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("No")
                }
            }
        )
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

