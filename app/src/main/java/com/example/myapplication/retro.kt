package com.example.myapplication

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val uid: String)

interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}