package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.notifications.ui.theme.NotificationScreen
import com.example.yourapp.ui.MyRequestsPage
import com.example.yourapp.ui.Request
import com.example.yourapp.ui.RequestStatus

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
//              Finallayout()
//              NotificationScreen()

                val sampleRequests = listOf(
                    Request("8m ago", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", RequestStatus.PENDING),
                    Request("10 days ago", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", RequestStatus.APPROVED),
                    Request("15 days ago", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", RequestStatus.DENIED)
                )
                MyRequestsPage(requests = sampleRequests)

            }
        }
    }
}




