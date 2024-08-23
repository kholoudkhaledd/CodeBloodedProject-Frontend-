package com.example.myapplication.Requests

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


fun fetchRequests(userId: String, onResult: (List<Request>) -> Unit) {
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



