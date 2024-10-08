package com.example.myapplication.Retrofit

import com.example.myapplication.NotificationSreen.NotificationData
import com.example.myapplication.Requests.Request
import com.example.myapplication.Requests.RequestStatus
import com.example.myapplication.calander.CalendarResponse
import com.example.myapplication.calander.DayScheduleResponse
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

data class Request(
    val id: String,
    val username: String,
    val time: String, // e.g., "2024-08-23 14:30:00"
    val changeDayFrom: String, // e.g., "2024-08-25 09:00:00"
    val changeDayTo: String, // e.g., "2024-08-26 17:00:00"
    val status: RequestStatus
)


data class CreateRequest(val userID: String, val changeDayFrom: String, val changeDayTo: String, val Status: String, val timeStamp: String)
data class UpdateStatusModel(val Status: String)
data class notifTokenModel (val notifToken: String)
data class MessageRequest(val message: String)
data class MessageResponse(val message: String)
data class OfficeCapacityResponse(val date: String, val office_capacity: Int)


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
        @Path("uid") userId: String, @Body notifToken: notifTokenModel
    )
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

    @POST("chatbot/{uid}")
    suspend fun sendMessage(@Path("uid") uid: String, @Body request: String): Response<MessageResponse>


    @GET("/view_schedule/{month}/{day}/{current_year}")
    suspend fun getDaySchedule(
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("current_year") year: String,
        @Header("Authorization") token: String
    ): DayScheduleResponse
    @GET("/most_requested_day")
    fun getMostRequestedDay(): Call<Map<String, Int>>  // Map of day names to counts

    @GET("/most_active_projects")
    fun getMostActiveProjects(): Call<Map<String, Int>>

    @GET("/most_active_users")
    fun getMostActiveUsers(): Call<List<Map<String, Any>>>  // List of users with counts

    @GET("/todays_office_capacity")
    fun getTodaysOfficeCapacity(): Call<OfficeCapacityResponse>

    @GET("/most_swapped_day")
    fun getMostSwappedDay(): Call<Map<String, String>>

    @POST("managerChatbot/{uid}")
    suspend fun sendMessageManager(@Path("uid") uid: String, @Body request: String): Response<MessageResponse>

}