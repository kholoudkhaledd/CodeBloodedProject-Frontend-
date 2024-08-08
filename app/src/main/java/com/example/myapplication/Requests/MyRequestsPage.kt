package com.example.yourapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Request(
    @SerializedName("requestID") val id: String,
    @SerializedName("Time Stamp") val time: String,
    @SerializedName("changeDayFrom") val changeDayFrom: String,
    @SerializedName("changeDayTo") val changeDayTo: String,
    @SerializedName("Status") val status: RequestStatus
)

enum class RequestStatus {
    @SerializedName("Pending") PENDING,
    @SerializedName("Approved") APPROVED,
    @SerializedName("Denied") DENIED
}

@Composable
fun MyRequestsPage() { //separate files
    var requestList by remember { mutableStateOf(listOf<Request>()) }

    LaunchedEffect(Unit) {
        fetchRequests { fetchedRequests ->
            requestList = fetchedRequests
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) //colors
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "My Requests",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .padding(top = 10.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(requestList) { request ->
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .alpha(0.5f)
                        )
                        RequestItem(
                            request = request,
                            onCancelRequest = { requestToCancel ->
                                deleteRequest(requestToCancel.id) {
                                    requestList = requestList.filter { it.id != requestToCancel.id }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

private fun fetchRequests(onResult: (List<Request>) -> Unit) {
    RetrofitClient.apiService.getRequests().enqueue(object : Callback<List<Request>> {
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let { onResult(it) }
            }
        }

        override fun onFailure(call: Call<List<Request>>, t: Throwable) {
            // Handle failure
        }
    })
}

private fun deleteRequest(requestId: String, onResult: () -> Unit) {
    RetrofitClient.apiService.deleteRequest(requestId).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                onResult()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            // Handle failure
        }
    })
}

@Composable
fun RequestItem(request: Request, onCancelRequest: (Request) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = request.time,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "From: ${request.changeDayFrom}",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = "To: ${request.changeDayTo}",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        when (request.status) {
            RequestStatus.PENDING -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = "Cancel Request",
                        tint = Color(0xFF76B31B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Cancel Request",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF76B31B),
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            onCancelRequest(request)
                        }
                    )
                }
            }
            RequestStatus.APPROVED -> {
                StatusBox(text = "Approved", backgroundColor = Color(0xFF19C588))
            }
            RequestStatus.DENIED -> {
                StatusBox(text = "Denied", backgroundColor = Color(0xFFF44336))
            }
        }
    }
}

@Composable
fun StatusBox(text: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 18.sp)
    }
}