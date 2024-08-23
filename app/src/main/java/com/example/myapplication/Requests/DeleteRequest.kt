package com.example.myapplication.Requests

import android.util.Log
import com.example.myapplication.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun DeleteRequest(requestId: String, onResult: () -> Unit) {
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



