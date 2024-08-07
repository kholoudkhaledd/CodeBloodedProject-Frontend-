package com.example.yourapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
<<<<<<< Updated upstream
import androidx.compose.material3.*
import androidx.compose.runtime.*
=======
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
>>>>>>> Stashed changes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.RequestsViewModel
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

<<<<<<< Updated upstream
data class Request(val time: String, val description: String, val status: RequestStatus)
=======
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RequestService by lazy {
        retrofit.create(RequestService::class.java)
    }
}

interface RequestService {
    @GET("http://10.0.2.2:8000/view_requests/")
    suspend fun getRequests(): List<RequestDto>

    @DELETE("/delete_request/{request_id}")
    suspend fun deleteRequest(@Path("request_id") requestId: String): Response

    @PUT("/update_status/{document_id}")
    suspend fun updateStatus(
        @Path("document_id") documentId: String,
        @Body statusUpdate: UpdateStatusDto
    ): Response
}
data class RequestDto(
    val changeDayFrom: String,
    val requestID: String,
    val changeDayTo: String,
    val Status: String,
    val userID: String,
    val TimeStamp: String
)

data class Request(
    val id: String,
    val time: String,
    val description: String,
    val status: RequestStatus
)


data class UpdateStatusDto(val Status: String)
>>>>>>> Stashed changes

enum class RequestStatus {
    PENDING, APPROVED, DENIED
}


@Composable
<<<<<<< Updated upstream
fun MyRequestsPage(requests: List<Request>) {
    var requestList by remember { mutableStateOf(requests) }
=======
fun MyRequestsPage() {
    val viewModel: RequestsViewModel = viewModel()
    val requests by viewModel.requests.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRequests()
    }
>>>>>>> Stashed changes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
<<<<<<< Updated upstream
            .padding(16.dp),
=======
            .statusBarsPadding(),
>>>>>>> Stashed changes
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),

            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "My Requests",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Divider(
                    modifier =
                    Modifier.padding(vertical = 8.dp)
                        .alpha(0.5f)
                )

<<<<<<< Updated upstream
                requestList.forEachIndexed { index, request ->
                    if (index > 0) {
                        Divider(modifier =
                        Modifier.padding(vertical = 8.dp)
                            .alpha(0.5f))
=======
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(requests) { request ->
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .alpha(0.5f)
                        )
                        RequestItem(
                            request = request,
                            onCancelRequest = { requestToCancel ->
                                viewModel.cancelRequest(requestToCancel)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
>>>>>>> Stashed changes
                    }
                    RequestItem(
                        request = request,
                        onCancelRequest = { requestToCancel ->
                            requestList = requestList.filter { it != requestToCancel }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
@Composable
fun RequestItem(request: Request, onCancelRequest: (Request) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = request.description, // Updated message format
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Scheduled from ${request.time}",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp)) // Increased space here
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
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .width(76.dp)
            .height(19.dp)
            .fillMaxSize()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 18.sp)
    }
}
