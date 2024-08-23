package com.example.myapplication.ManagarRequestsScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.Requests.Request
import com.example.myapplication.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
fun fetchRequests(onResult: (List<Request>) -> Unit) {
    RetrofitClient.apiService.getAllRequests().enqueue(object : Callback<List<Request>> {
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let { requests ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    // Sort the requests by time in descending order
                    val sortedRequests = requests.sortedByDescending { request ->
                        try {
                            // Parse the timestamp string into a LocalDateTime object
                            val dateTime = LocalDateTime.parse(request.time, formatter)
                            // Convert LocalDateTime to Instant
                            dateTime.atZone(ZoneId.systemDefault()).toInstant()
                        } catch (e: DateTimeParseException) {
                            Log.e("fetchRequests", "Failed to parse timestamp: ${request.time}")
                            Instant.MIN // Fallback for sorting purposes if parsing fails
                        }
                    }
                    Log.d("fetchRequests", "Fetched requests: $sortedRequests")
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
