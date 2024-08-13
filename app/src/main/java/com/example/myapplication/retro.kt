package com.example.myapplication

import com.example.myapplication.calander.CalendarResponse
import com.example.yourapp.ui.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(
    val uid: String,
    val username: String,
    val position: String?
)


data class CreateRequest(val userID: String, val changeDayFrom: String, val changeDayTo: String, val Status: String, val timeStamp: String)
data class UpdateStatusModel(val Status: String)


interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("view_requests/{uid}")
    fun getRequests(@Path("uid") userId: String): Call<List<Request>>

    @GET("view_all_requests")
    fun getAllRequests(): Call<List<Request>>

    @POST("create_request/{uid}")
    fun createRequest(@Path("uid") userId: String, @Body request: CreateRequest): Call<Void>

    @DELETE("delete_request/{request_id}")
    fun deleteRequest(@Path("request_id") requestId: String): Call<Void>

    @PUT("update_status/{document_id}")
    fun updateStatus(@Path("document_id") documentId: String, @Body statusUpdate: UpdateStatusModel): Call<Void>
    
    @GET("view_schedule/{userId}/{date}")
    fun getCalendarForMonth(
        @Path("userId") userId: String,
        @Path("date") date: String
    ): Call<CalendarResponse>
}