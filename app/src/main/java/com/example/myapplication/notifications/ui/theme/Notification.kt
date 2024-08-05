package com.example.myapplication.notifications.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
            .background(Color(0xFFF5F5F5)) // Background color matching MyRequestsPage
            .statusBarsPadding()
    ) {
        Card(
            shape = RoundedCornerShape(32.dp), // Rounded corners for the outer card
            modifier = Modifier
                .fillMaxSize() // Fill max width of the screen
                .padding(bottom = 24.dp), // Padding to ensure the card background is visible
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(16.dp) // Padding around the title
                        .align(Alignment.CenterHorizontally)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(notificationItems) { notification ->
                        NotificationCard(
                            backgroundColor = notification.backgroundColor,
                            stripeColor = notification.stripeColor,
                            icon = notification.icon,
                            text = notification.text
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    backgroundColor: Color,
    stripeColor: Color,
    icon: Int,
    text: String
) {
    Card(
        shape = RoundedCornerShape(0.dp), // Rectangular corners
        modifier = Modifier
            .fillMaxWidth() // Fill max width of the screen
            .height(90.dp), // Fixed height for all cards
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row() {
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
                    .size(32.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(9.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun NotificationScreenPreview() {
    NotificationScreen()
}

// Example data for preview
private val notificationItems = listOf(
    NotificationData(
        backgroundColor = Color(0xFFE8F5E9),
        stripeColor = Color(0xFF19C588),
        icon = R.drawable.icon_check,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
    ),
    NotificationData(
        backgroundColor = Color(0xFFFFEBEE),
        stripeColor = Color(0xFFF44336),
        icon = R.drawable.icon_deny,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
    ),    NotificationData(
        backgroundColor = Color(0xFFE8F5E9),
        stripeColor = Color(0xFF19C588),
        icon = R.drawable.icon_check,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
    ),
    NotificationData(
        backgroundColor = Color(0xFFFFEBEE),
        stripeColor = Color(0xFFF44336),
        icon = R.drawable.icon_deny,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
    ),    NotificationData(
        backgroundColor = Color(0xFFE8F5E9),
        stripeColor = Color(0xFF19C588),
        icon = R.drawable.icon_check,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
    ),
    NotificationData(
        backgroundColor = Color(0xFFFFEBEE),
        stripeColor = Color(0xFFF44336),
        icon = R.drawable.icon_deny,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis."
    )
)

data class NotificationData(
    val backgroundColor: Color,
    val stripeColor: Color,
    val icon: Int,
    val text: String
)
