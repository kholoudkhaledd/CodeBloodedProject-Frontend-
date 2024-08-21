package com.example.myapplication.NotificationSreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.myapplication.R
import com.example.myapplication.Requests.Request
import com.example.myapplication.Requests.RequestStatus
import com.example.myapplication.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 fun fetchAllNotifications(onResult: (List<NotificationData>) -> Unit) {
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
