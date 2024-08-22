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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.myapplication.Requests.Request
import com.example.myapplication.Requests.RequestStatus
import com.example.myapplication.Requests.StatusBox
import com.example.myapplication.Requests.formatDate
import com.example.myapplication.Requests.timeAgo
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.Retrofit.UpdateStatusModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private fun fetchRequests(onResult: (List<Request>) -> Unit) {
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
    var showDialog by remember { mutableStateOf(false) }
    var currentRequest by remember { mutableStateOf<Request?>(null) }
    var isApproving by remember { mutableStateOf(true) } // true for approving, false for denying

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
                    // Triggering a fetch to refresh the list
                    fetchRequests { fetchedRequests ->
                        Log.d("approveRequest", "Fetched updated requests: $fetchedRequests")
                        requestList = fetchedRequests
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
                    // Triggering a fetch to refresh the list
                    fetchRequests { fetchedRequests ->
                        Log.d("denyRequest", "Fetched updated requests: $fetchedRequests")
                        requestList = fetchedRequests
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

    // Function to show the confirmation dialog
    fun showConfirmationDialog(request: Request, approve: Boolean) {
        currentRequest = request
        isApproving = approve
        showDialog = true
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
                            onApproveRequest = { showConfirmationDialog(request, true) },
                            onDenyRequest = { showConfirmationDialog(request, false) }
                        )
                    }
                }
            }
        }
    }

    // Confirmation Dialog
    if (showDialog && currentRequest != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Action") },
            text = {
                Text(
                    text = if (isApproving) {
                        "Are you sure you want to approve this request?"
                    } else {
                        "Are you sure you want to deny this request?"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isApproving) {
                            currentRequest?.let { approveRequest(it) }
                        } else {
                            currentRequest?.let { denyRequest(it) }
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isApproving) Color(0xFF19C588) else Color(0xFFFEB5757))
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("No")
                }
            }
        )
    }
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
        ) {
            Text(
                text = request.username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Start,
            )
            Text(
                text = timeAgo(request.time),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.LightGray,
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Request to swap work locations between ${formatDate(request.changeDayFrom)} & ${formatDate(request.changeDayTo)}",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (request.status == RequestStatus.PENDING) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_deny),
                    contentDescription = "Deny Request",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onDenyRequest(request) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.icon_check),
                    contentDescription = "Approve Request",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onApproveRequest(request) }
                )
            }
        } else {
            if (isApproved) {
                StatusBox(text = "Approved", backgroundColor = Color(0xFF19C588))
            } else {
                StatusBox(text = "Denied", backgroundColor = Color(0xFFFEB5757))
            }
        }
    }
}

