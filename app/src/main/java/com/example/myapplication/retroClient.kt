package com.example.myapplication

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/" // Replace with your backend URL
    private var userId: String = ""
    private var token: String = ""
        private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(userId, token))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
        fun updateAuth(userId: String, token: String) {
        this.userId = userId
        this.token = token
    }

}
//    fun updateAuth(userId: String, token: String) {
//        this.userId = userId
//        this.token = token
//    }
//object RetrofitClient {
//    private const val BASE_URL = "http://10.0.2.2:8000/"
//
//    // Assuming you will get userId and token from the login response or stored preferences
//    private var userId: String = ""
//    private var token: String = ""
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor(userId, token))
//        .build()
//
//    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val apiService: ApiService = retrofit.create(ApiService::class.java)
//
//    // Method to update userId and token
//    fun updateAuth(userId: String, token: String) {
//        this.userId = userId
//        this.token = token
//    }
//}
