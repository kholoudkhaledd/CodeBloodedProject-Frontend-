package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.myapplication.calander.Finallayout
import com.example.myapplication.notifications.ui.theme.NotificationScreen
import com.example.myapplication.ui.theme.ChatScreenPreview
import com.example.myapplication.ui.theme.GreenJC
import com.example.myapplication.ui.theme.MyApplicationTheme
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
                NavigationScreen()
            }
        }
    }
}

@Composable
fun CustomBottomNavigationBar(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFECECEC))
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BarIcon(
                    selected = selectedScreen == Screens.Home.screen,
                    iconId = if (selectedScreen == Screens.Home.screen) R.drawable.calendergreen else R.drawable.calendergray,
                    contentDescription = "Home",
                    onClick = { onScreenSelected(Screens.Home.screen) }
                )

                BarIcon(
                    selected = selectedScreen == Screens.Chatbot.screen,
                    iconId = if (selectedScreen == Screens.Chatbot.screen) R.drawable.chatggreen else R.drawable.chatgray,
                    contentDescription = "Chatbot",
                    onClick = { onScreenSelected(Screens.Chatbot.screen) }
                )

                BarIcon(
                    selected = selectedScreen == Screens.Requests.screen,
                    iconId = if (selectedScreen == Screens.Requests.screen) R.drawable.requestgreen else R.drawable.requestgray,
                    contentDescription = "Requests",
                    onClick = { onScreenSelected(Screens.Requests.screen) }
                )

                BarIcon(
                    selected = selectedScreen == Screens.Notification.screen,
                    iconId = if (selectedScreen == Screens.Notification.screen) R.drawable.notifygreen else R.drawable.notifygray,
                    contentDescription = "Notifications",
                    onClick = { onScreenSelected(Screens.Notification.screen) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val selectedScreen = remember { mutableStateOf(Screens.Home.screen) }

    Scaffold(
        bottomBar = {
            CustomBottomNavigationBar(
                selectedScreen = selectedScreen.value,
                onScreenSelected = { screen ->
                    selectedScreen.value = screen
                    navController.navigate(screen) {
                        popUpTo(screen) { inclusive = true }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.screen,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.Home.screen) { Finallayout() }
            composable(Screens.Notification.screen) { NotificationScreen() }
            composable(Screens.Chatbot.screen) { ChatScreenPreview() }
            composable(Screens.Requests.screen) {
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

@Composable
fun BarIcon(
    selected: Boolean,
    iconId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(Color.Transparent) // Ensures background is transparent
    ) {
        val iconPainter = painterResource(id = iconId)
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription,
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp) // Adjust size of the icon as needed
                .padding(bottom = 8.dp), // Adds space for the dot indicator
            tint = if (selected) Color(0xFF76B31B) else Color(0xFF8F8EA2) // Change color based on selection
        )

        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Align dot at the bottom center of the icon
                    .size(4.dp) // Size of the dot
                    .background(Color(0xFF76B31B), shape = CircleShape) // Color and shape of the dot
            )
        }
    }
}
