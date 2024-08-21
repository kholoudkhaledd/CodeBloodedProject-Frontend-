package com.example.myapplication.Requests

import com.google.gson.annotations.SerializedName


data class Request(
    @SerializedName("requestID") val id: String = "",
    @SerializedName("timeStamp") val time: String = "",
    @SerializedName("changeDayFrom") val changeDayFrom: String = "",
    @SerializedName("changeDayTo") val changeDayTo: String = "",
    @SerializedName("Status") var status: RequestStatus = RequestStatus.PENDING,
    @SerializedName("username") val username: String = ""

)

enum class RequestStatus {
    @SerializedName("Pending") PENDING,
    @SerializedName("Approved") APPROVED,
    @SerializedName("Denied") DENIED
}
