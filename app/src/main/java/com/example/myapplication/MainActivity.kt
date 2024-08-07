package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
//                val sampleRequests = listOf(
//                    Request(id = 1, time = "8m ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.APPROVED),
//                    Request(id = 2, time = "8m ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.APPROVED),
//                    Request(id = 3, time = "10 days ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.DENIED),
//                    Request(id = 4, time = "15 days ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.DENIED),
//                    Request(id = 5, time = "20 days ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.DENIED)
//                )

//                ManagerRequest(requests = sampleRequests)

                NavigationScreen()
            }
        }
    }
}
