package com.example.myapplication.NotificationSreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.R
import com.example.myapplication.Requests.Request
import com.example.myapplication.Requests.RequestStatus
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.ui.theme.greennotfication
import com.example.myapplication.ui.theme.greennotfication2
import com.example.myapplication.ui.theme.rednotification
import com.example.myapplication.ui.theme.rednotification2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun fetchNotifications(userId: String, onResult: (List<NotificationData>) -> Unit) {
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
                                backgroundColor = if (request.status == RequestStatus.APPROVED) greennotfication else rednotification,
                                stripeColor = if (request.status == RequestStatus.APPROVED) greennotfication2 else rednotification2,
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
