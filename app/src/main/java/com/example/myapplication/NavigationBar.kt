package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background

import androidx.compose.runtime.Composable

import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.notifications.ui.theme.NotificationScreen
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.myapplication.Screens
import com.example.myapplication.calander.Finallayout
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
//
                NavigationBar()
//                Finallayout()
            }
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationBar() {
    val navController = rememberNavController()
    val selectedScreen = remember { mutableStateOf(Screens.Home.screen) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.background(Color.White).height(56.dp)
            ) {
                // Home button
                IconButton(
                    onClick = {
                        selectedScreen.value = Screens.Home.screen
                        navController.navigate(Screens.Home.screen) {
                            popUpTo(Screens.Home.screen) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    val icon = if (selectedScreen.value == Screens.Home.screen) painterResource(id = R.drawable.calandergreen) else painterResource(id = R.drawable.calandergray)
                    Icon(
                        painter = icon,
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Chatbot button
                IconButton(
                    onClick = {
                        selectedScreen.value = Screens.Chatbot.screen
                        navController.navigate(Screens.Chatbot.screen) {
                            popUpTo(Screens.Chatbot.screen) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    val icon = if (selectedScreen.value == Screens.Chatbot.screen) painterResource(id = R.drawable.chatggreen) else painterResource(id = R.drawable.chatgray)
                    Icon(
                        painter = icon,
                        contentDescription = "Chatbot",
                        modifier = Modifier.size(28.dp)
                    )
                }
                // Requests button
                IconButton(
                    onClick = {
                        selectedScreen.value = Screens.Requests.screen
                        navController.navigate(Screens.Requests.screen) {
                            popUpTo(Screens.Requests.screen) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    val icon = if (selectedScreen.value == Screens.Requests.screen) painterResource(id = R.drawable.requestgreen) else painterResource(id = R.drawable.requestgray)
                    Icon(
                        painter = icon,
                        contentDescription = "Requests",
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Notifications button
                IconButton(
                    onClick = {
                        selectedScreen.value = Screens.Notification.screen
                        navController.navigate(Screens.Notification.screen) {
                            popUpTo(Screens.Notification.screen) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    val icon = if (selectedScreen.value == Screens.Notification.screen) painterResource(id = R.drawable.notifygreen) else painterResource(id = R.drawable.notifygray)
                    Icon(
                        painter = icon,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.screen,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.Home.screen) { Finallayout() }
            composable(Screens.Notification.screen) { NotificationScreen() }
            composable(Screens.Chatbot.screen) { Chatbot() }
            composable(Screens.Requests.screen) { val sampleRequests = listOf(
                Request("8m ago", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", RequestStatus.PENDING),
                Request("10 days ago", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", RequestStatus.APPROVED),
                Request("15 days ago", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", RequestStatus.DENIED)
            )
                MyRequestsPage(requests = sampleRequests) }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun NotificationScreenPreview() {
    NavigationBar()
}







