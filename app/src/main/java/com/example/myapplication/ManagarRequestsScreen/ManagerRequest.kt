package com.example.myapplication.ManagarRequestsScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.Requests.Request
import com.example.myapplication.Requests.RequestStatus
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.Retrofit.UpdateStatusModel
import com.example.myapplication.ui.theme.BackgroundPagesColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



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
            .background(BackgroundPagesColor),
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
                    text = stringResource(id = R.string.Requests),
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






