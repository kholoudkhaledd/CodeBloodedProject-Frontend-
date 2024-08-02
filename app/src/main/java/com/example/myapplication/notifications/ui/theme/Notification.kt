package com.example.myapplication.notifications.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun NotificationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(20.dp)
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Text(
            text = "Notifications",
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp, top = 20.dp)
        )
        NotificationCard(
            backgroundColor = Color(0xFFE8F5E9),
            stripeColor = Color(0xFF19C588),
            icon = R.drawable.icon_check,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
        )
        Spacer(modifier = Modifier.height(16.dp))
        NotificationCard(
            backgroundColor = Color(0xFFFFEBEE),
            stripeColor = Color(0xFFF44336),
            icon = R.drawable.icon_deny,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
        )
    }
}

@Composable
fun NotificationCard(
    backgroundColor: Color,
    stripeColor: Color,
    icon: Int,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(stripeColor)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterVertically)


        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp)
        )
    }
}

@Preview
@Composable
fun NotificationScreenPreview() {
    NotificationScreen()
}