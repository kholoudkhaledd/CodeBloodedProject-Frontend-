package com.example.myapplication

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback


class PushNotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("PushNotificationService", "From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("PushNotificationService", "Message data payload: ${remoteMessage.data}")
        }
        remoteMessage.notification?.let {
            Log.d("PushNotificationService", "Message Notification Body: ${it.body}")
        }
    }
    fun updateNotificationToken(userId: String, notifToken: String) {
        val tokenUpdate = notifTokenModel(notifToken)

        RetrofitClient.apiService.update_notif_token(userId, tokenUpdate).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("updateNotificationToken", "Notification token updated successfully")
                } else {
                    Log.e("updateNotificationToken", "Failed to update notification token: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("updateNotificationToken", "Error updating notification token", t)
            }
        })
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val userID = Sharedpreference.getUserId(this.baseContext)

        if (userID != null) {
            updateNotificationToken(userID, token)
        }

        Log.d("PushNotificationService", "Refreshed token: $token")

    }
}