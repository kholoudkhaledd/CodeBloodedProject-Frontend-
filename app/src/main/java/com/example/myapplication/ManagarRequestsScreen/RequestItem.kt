package com.example.myapplication.ManagarRequestsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            text =stringResource(id = R.string.Request_to_swap_work_locations_between_the)+{formatDate(request.changeDayFrom)}+stringResource(id = R.string.and_the)+{formatDate(request.changeDayTo)},
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
                StatusBox(text = stringResource(id = R.string.Approved), backgroundColor = Color(0xFF19C588))
            } else{
                StatusBox(text = stringResource(id = R.string.Denied), backgroundColor = Color(0xFFFEB5757))
            }
        }
    }
}