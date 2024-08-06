package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.GreenJC
import com.example.yourapp.ui.Request

@Composable
fun ManagerRequest(requests: List<Request>) {
    var requestList by remember { mutableStateOf(requests) }

    // Functions to handle approval and denial of requests
    fun approveRequest(request: Request) {
        // Handle request approval logic here
    }

    fun denyRequest(request: Request) {
        // Handle request denial logic here
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
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
                            .padding(vertical = 8.dp)
                            .alpha(0.5f)
                    )
                    RequestItem(
                        request = request,
                        onApproveRequest = { approveRequest(request) },
                        onDenyRequest = { denyRequest(request) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun RequestItem(
    request: Request,
    onApproveRequest: (Request) -> Unit,
    onDenyRequest: (Request) -> Unit
) {
    var isApproved by remember { mutableStateOf(false) }
    var isDenied by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = request.time,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = request.description,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (!showMessage) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_deny),
                    contentDescription = "Deny Request",
                    tint = Color.Unspecified, // Remove any tint
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            onDenyRequest(request)
                            isDenied = true
                            isApproved = false
                            showMessage = true
                            message = "You have denied the request"
                        }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.icon_check),
                    contentDescription = "Approve Request",
                    tint = Color.Unspecified, // Remove any tint
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            onApproveRequest(request)
                            isApproved = true
                            isDenied = false
                            showMessage = true
                            message = "You have accepted the request"
                        }
                )
            }
        } else {
            Text(
                text = message,
                color = if (isApproved) {
                    colorResource(id = R.color.deloitteGreen)
                } else Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
