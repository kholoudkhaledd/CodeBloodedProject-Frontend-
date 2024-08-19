package com.example.myapplication

import com.example.myapplication.calander.CalendarResponse
import com.example.myapplication.manager.TeamsCalendarResponse
import com.example.myapplication.notifications.ui.theme.NotificationData
import com.example.yourapp.ui.Request
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(
    val uid: String,
    val username: String,
    val position: String,
    val token_id: String

)


data class CreateRequest(val userID: String, val changeDayFrom: String, val changeDayTo: String, val Status: String, val timeStamp: String)
data class UpdateStatusModel(val Status: String)
data class notifTokenModel (val notifToken: String)


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
    fun updateStatus(@Path("document_id") documentId: String, @Body statusUpdate: UpdateStatusModel)
            : Call<Void>

    @PUT("update_notif_token/{uid}")
    fun update_notif_token(
        @Path("uid") userId: String, @Body notifToken: notifTokenModel)
            : Call<Void>


    @GET("view_schedule/{date}")
    fun getCalendarForMonth(
        @Path("date") date: String,
        @Header("Authorization") token: String
    ): Call<CalendarResponse>


    @GET("view_notification/{uid}")
    fun getNotifications(@Path("uid") userId: String): Call<List<NotificationData>>

    @GET("view_teams_schedule/{Name}/{date}")
    fun getCalendarForTeam(
        @Path("Name") name: String,
        @Path("date") date: String
    ): Call<CalendarResponse>

    @GET("get_all_usernames") // Replace with your actual endpoint
    fun getAllUsernames(): Call<List<String>>
    @POST("/checktoken/")
    suspend fun checkToken(@Header("Authorization") token: String): Response<String>

    @POST("generate_monthly_schedule/")
    fun updateTeamSchedule(
        @Query("officeDays1") officeDays1: Int,
        @Query("officeDays2") officeDays2: Int
    ): Call<Void>


}