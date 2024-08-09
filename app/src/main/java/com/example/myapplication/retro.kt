package com.example.myapplication

import com.example.myapplication.calander.CalendarResponse
import com.example.yourapp.ui.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val uid: String,val  username:String)

interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("view_requests/")
    fun getRequests(): Call<List<Request>>

    @DELETE("delete_request/{request_id}")
    fun deleteRequest(@Path("request_id") requestId: String): Call<Void>

    @GET("view_schedule/{userId}/{date}")

    fun getCalendarForMonth(
        @Path("userId") userId: String,
        @Path("date") date: String
    ): Call<CalendarResponse>


}