package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.myapplication.notifications.ui.theme.NotificationScreen
import com.example.myapplication.calander.Finallayout
import com.example.myapplication.ui.theme.GreenJC
import com.example.yourapp.ui.MyRequestsPage
import com.example.yourapp.ui.Request
import com.example.yourapp.ui.RequestStatus
import com.example.myapplication.ui.theme.Chatbot

@Composable
fun CustomBottomNavigationBar(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .height(56.dp), // Height of the bottom bar
        horizontalArrangement = Arrangement.SpaceAround, // Distribute space evenly
        verticalAlignment = Alignment.CenterVertically // Center icons vertically
    ) {
        // Home button
        BarIcon(
            selected = selectedScreen == Screens.Home.screen,
            iconId = if (selectedScreen == Screens.Home.screen) R.drawable.calandergreen else R.drawable.calandergray,
            contentDescription = "Home",
            onClick = { onScreenSelected(Screens.Home.screen) }

        )
        // Chatbot button
        BarIcon(
            selected = selectedScreen == Screens.Chatbot.screen,
            iconId = if (selectedScreen == Screens.Chatbot.screen) R.drawable.chatggreen else R.drawable.chatgray,
            contentDescription = "Chatbot",
            onClick = { onScreenSelected(Screens.Chatbot.screen) }
        )

        // Requests button
        BarIcon(
            selected = selectedScreen == Screens.Requests.screen,
            iconId = if (selectedScreen == Screens.Requests.screen) R.drawable.requestgreen else R.drawable.requestgray,
            contentDescription = "Requests",
            onClick = { onScreenSelected(Screens.Requests.screen) }
        )

        // Notifications button
        BarIcon(
            selected = selectedScreen == Screens.Notification.screen,
            iconId = if (selectedScreen == Screens.Notification.screen) R.drawable.notifygreen else R.drawable.notifygray,
            contentDescription = "Notifications",
            onClick = { onScreenSelected(Screens.Notification.screen) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    var selectedScreen by remember { mutableStateOf(Screens.SplashScreen.screen) }

    Scaffold(
        bottomBar = {
            if (selectedScreen !in listOf(Screens.SplashScreen.screen, Screens.Login.screen)) {
                CustomBottomNavigationBar(
                    selectedScreen = selectedScreen,
                    onScreenSelected = { screen ->
                        selectedScreen = screen
                        navController.navigate(screen) {
                            popUpTo(screen) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.SplashScreen.screen,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            composable(Screens.SplashScreen.screen) { SplashScreen(navController) }
            composable(Screens.Login.screen) { LoginScreen(navController) }
            composable(Screens.Home.screen) {
                selectedScreen = Screens.Home.screen
                Finallayout()
            }
            composable(Screens.Notification.screen) { NotificationScreen() }
            composable(Screens.Chatbot.screen) { Chatbot() }
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
            .size(70.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(Color.Transparent) // Ensures background is transparent
    ) {
        // Icon
        val iconPainter = painterResource(id = iconId)
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription,
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp) // Adjust size of the icon as needed
                .padding(bottom = 8.dp), // Adds space for the dot indicator
            tint = if (selected) GreenJC else Color.Gray // Change color based on selection
        )

        // Dot indicator
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Align dot at the bottom center of the icon
                    .size(8.dp) // Size of the dot
                    .background(GreenJC, shape = CircleShape) // Color and shape of the dot
            )
        }
    }
}
