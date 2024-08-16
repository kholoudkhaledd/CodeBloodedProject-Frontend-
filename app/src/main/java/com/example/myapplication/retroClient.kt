package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

   //private const val BASE_URL = "http://10.0.2.2:8000/" // Replace with your backend URL
  private const val BASE_URL = "http://192.168.1.13:8000/" // Replace with your backend URL.0.2.2:8000/" // Replace with your backend URL

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
