package com.example.myapplication


import ChatScreen
import SharedViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.calander.Finallayout
import com.example.myapplication.notifications.ui.theme.NotificationScreen
import com.example.yourapp.ui.MyRequestsPage
import com.example.yourapp.ui.Request
import com.example.yourapp.ui.RequestStatus


@Composable
fun CustomBottomNavigationBar(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .height(56.dp) // Height of the bottom bar
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround, // Distribute space evenly
            verticalAlignment = Alignment.CenterVertically // Center icons vertically
        ) {
            // Home button
            BarIcon(
                selected = selectedScreen == Screens.Home.screen,
                iconId = if (selectedScreen == Screens.Home.screen) R.drawable.calendergreen else R.drawable.calendergray,
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
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
            composable(Screens.SplashScreen.screen) { SplashScreen(navController, sharedViewModel) }
            composable(Screens.Login.screen) {
                val context = LocalContext.current
                LoginScreen(navController, context,sharedViewModel)
            }
            composable(Screens.Home.screen) {
                selectedScreen = Screens.Home.screen
                val context = LocalContext.current
                Finallayout(context )
            }
            composable(Screens.Chatbot.screen) { ChatScreen() }
            composable(Screens.Notification.screen) { NotificationScreen() }
            composable(Screens.Requests.screen) {
                val sampleRequests = listOf(
                    Request(id = 1, time = "8m ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.PENDING),
                    Request(id = 2, time = "8m ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.PENDING),
                    Request(id = 3, time = "10 days ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.APPROVED),
                    Request(id = 4, time = "15 days ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.APPROVED),
                    Request(id = 5, time = "20 days ago", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu faci", status = RequestStatus.DENIED)
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
                .background(Color.Transparent) //hoisting plz
        ) {
            val iconPainter = painterResource(id = iconId)
            Icon(
                painter = iconPainter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
                    .padding(bottom = 8.dp),
                tint = if (selected) Color(0xFF76B31B) else Color(0xFF8F8EA2)
            )

            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(4.dp)
                        .background(Color(0xFF76B31B), shape = CircleShape)
                )
            }
        }
    }