package com.example.myapplication.Teamschedulescreen

import com.example.myapplication.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun changeTeamSchedule(officeDays1:Int, officeDays2:Int){
    RetrofitClient.apiService.updateTeamSchedule(officeDays1,officeDays2)
        .enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    println("Success")
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("Error: ${response.code()}, Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
                println("Request Failed: ${t.message}")
            }
        })
}
