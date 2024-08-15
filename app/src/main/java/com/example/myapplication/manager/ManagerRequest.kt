package com.example.myapplication.manager

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.example.myapplication.UpdateStatusModel
import com.example.yourapp.ui.Request
import com.example.yourapp.ui.RequestStatus
import com.example.yourapp.ui.StatusBox
import com.example.yourapp.ui.formatDate
import com.example.yourapp.ui.timeAgo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private fun fetchRequests(userId: String, onResult: (List<Request>) -> Unit) {
    RetrofitClient.apiService.getAllRequests().enqueue(object : Callback<List<Request>> {
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("fetchRequests", "Fetched requests: $it")
                    onResult(it)
                }
            } else {
                Log.e("fetchRequests", "Failed to fetch requests: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Request>>, t: Throwable) {
            Log.e("fetchRequests", "Error fetching requests", t)
        }
    })
}
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ManagerRequest() {
    val context = LocalContext.current
    var requestList by remember { mutableStateOf(listOf<Request>()) }

    LaunchedEffect(Unit) {
        fetchRequests { fetchedRequests ->
            Log.d("ManagerRequest", "Fetched requests: $fetchedRequests")
            requestList = fetchedRequests
        }
    }

    // Functions to handle approval and denial of requests
    fun approveRequest(request: Request) {
        val statusUpdate = UpdateStatusModel(Status = "Approved")
        RetrofitClient.apiService.updateStatus(request.id, statusUpdate).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    requestList = requestList.map {
                        if (it.id == request.id) it.copy(status = RequestStatus.APPROVED) else it
                    }
                    Log.d("approveRequest", "Request approved successfully")
                } else {
                    Log.e("approveRequest", "Failed to approve request: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("approveRequest", "Error approving request", t)
            }
        })
    }

    fun denyRequest(request: Request) {
        val statusUpdate = UpdateStatusModel(Status = "Denied")
        RetrofitClient.apiService.updateStatus(request.id, statusUpdate).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    requestList = requestList.map {
                        if (it.id == request.id) it.copy(status = RequestStatus.DENIED) else it
                    }
                    Log.d("denyRequest", "Request denied successfully")
                } else {
                    Log.e("denyRequest", "Failed to deny request: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("denyRequest", "Error denying request", t)
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC)),
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
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Requests",
                    fontSize = 24.sp,
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
                                .padding(vertical = 16.dp)
                                .alpha(0.5f)
                        )
                        RequestItem(
                            request = request,
                            onApproveRequest = { approveRequest(request) },
                            onDenyRequest = { denyRequest(request) }
                        )
                    }
                }
            }
        }
    }
}

private fun fetchRequests(onResult: (List<Request>) -> Unit) {
    RetrofitClient.apiService.getAllRequests().enqueue(object : Callback<List<Request>> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("fetchRequests", "Fetched requests: $it")
                    // Sort the requests by timestamp in descending order (newest first)
                    val sortedRequests = it.sortedByDescending { request ->
                        Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()).parse(request.time))
                    }
                    onResult(sortedRequests)
                }
            } else {
                Log.e("fetchRequests", "Failed to fetch requests: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Request>>, t: Throwable) {
            Log.e("fetchRequests", "Error fetching requests", t)
        }
    })
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RequestItem(
    request: Request,
    onApproveRequest: (Request) -> Unit,
    onDenyRequest: (Request) -> Unit
) {
    val isApproved = request.status == RequestStatus.APPROVED
    val isDenied = request.status == RequestStatus.DENIED

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = request.username, // Display the username here
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Start,
            )
            Text(
                text = timeAgo(request.time), // Use the timeAgo function here
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.LightGray,
                textAlign = TextAlign.End


            )

        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Request to swap work locations between the ${formatDate(request.changeDayFrom)} & the ${formatDate(request.changeDayTo)}",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (!isApproved && !isDenied) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    painter = painterResource(id = R.drawable.icon_deny),
                    contentDescription = "Deny Request",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            onDenyRequest(request)
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.icon_check),
                    contentDescription = "Approve Request",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            onApproveRequest(request)
                        }
                )

            }
        } else {
            if (isApproved) {
                StatusBox(text = "Approved", backgroundColor = Color(0xFF19C588))
            } else{
                StatusBox(text = "Denied", backgroundColor = Color(0xFFFEB5757))
            }
        }
    }
}



