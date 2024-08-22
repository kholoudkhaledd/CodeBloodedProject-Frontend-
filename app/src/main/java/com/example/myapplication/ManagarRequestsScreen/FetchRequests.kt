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

fun fetchRequests(onResult: (List<Request>) -> Unit) {
    RetrofitClient.apiService.getAllRequests().enqueue(object : Callback<List<Request>> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("fetchRequests", "Fetched requests: $it")
                    // Sort the requests by timestamp in descending order (newest first)
                    val sortedRequests = it.sortedByDescending { request ->
                        Instant.from(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(
                                ZoneId.systemDefault()).parse(request.time))
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

