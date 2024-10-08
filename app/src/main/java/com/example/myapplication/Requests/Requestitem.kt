package com.example.myapplication.Requests

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RequestItem(request: Request, onCancelRequest: (Request) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = timeAgo(request.time),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text =stringResource(id = R.string.Request_to_swap_work_locations_between_the)+
                    " " +
                    formatDate(request.changeDayFrom) +
                    " " +
                    stringResource(id = R.string.and_the)+
                    " " +
                    formatDate(request.changeDayTo),
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
                        //text = "Cancel Request",
                        text= stringResource(id = R.string.Cancel_Request),
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
                StatusBox(text = stringResource(id = R.string.Approved), backgroundColor = Color(0xFF19C588))
            }
            RequestStatus.DENIED -> {
                StatusBox(text = stringResource(id = R.string.Denied), backgroundColor = Color(0xFFFEB5757))
            }
        }
    }
}