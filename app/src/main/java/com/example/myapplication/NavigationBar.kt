package com.example.myapplication


import SharedViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.calander.Finallayout
import com.example.myapplication.manager.TeamsScheduleScreen
import com.example.myapplication.notifications.ui.theme.NotificationScreen
import com.example.yourapp.ui.MyRequestsPage
import com.example.myapplication.Screens



@Composable
fun CustomBottomNavigationBar(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    additionalIcon: (@Composable () -> Unit)? = null // Add this parameter
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFECECEC))
            .height(80.dp)
            .clip(RoundedCornerShape(32.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
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
            additionalIcon?.invoke()

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
            // Optionally add the extra icon for managers
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    var selectedScreen by remember { mutableStateOf(Screens.SplashScreen.screen) }
    var isManager by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (selectedScreen !in listOf(Screens.SplashScreen.screen, Screens.Login.screen)) {
                if (isManager) {
                    CustomBottomNavigationBar(
                        selectedScreen = selectedScreen,
                        onScreenSelected = { screen ->
                            if (selectedScreen != screen) {
                                selectedScreen = screen
                                navController.navigate(screen) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        additionalIcon = {
                            BarIcon(
                                selected = selectedScreen == Screens.TeamsSchedule.screen,
                                iconId = if (selectedScreen == Screens.TeamsSchedule.screen) R.drawable.teamsgreen else R.drawable.teamsgray,
                                contentDescription = "Teams Schedule",
                                onClick = {
                                    if (selectedScreen != Screens.TeamsSchedule.screen) {
                                        selectedScreen = Screens.TeamsSchedule.screen
                                        navController.navigate(Screens.TeamsSchedule.screen) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    )
                } else {
                    CustomBottomNavigationBar(
                        selectedScreen = selectedScreen,
                        onScreenSelected = { screen ->
                            if (selectedScreen != screen) {
                                selectedScreen = screen
                                navController.navigate(screen) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
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
                LoginScreen(navController, context, sharedViewModel) { success, position ->
                    isManager = position.equals("Manager", ignoreCase = true)
                }
            }
            composable(Screens.Home.screen) {
                selectedScreen = Screens.Home.screen
                val context = LocalContext.current
                Finallayout(context)
            }
            composable(Screens.Chatbot.screen) {
                selectedScreen = Screens.Chatbot.screen
                ChatScreen()
            }
            composable(Screens.Notification.screen) {
                selectedScreen = Screens.Notification.screen
                NotificationScreen()
            }
            composable(Screens.Requests.screen) {
                selectedScreen = Screens.Requests.screen
                MyRequestsPage()
            }
            composable(Screens.TeamsSchedule.screen) {
                selectedScreen = Screens.TeamsSchedule.screen
                TeamsScheduleScreen()
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
    // Define default and expanded sizes
    val defaultSize = 50.dp
    val expandedSize = 64.dp

    // Animate size change
    val size by animateDpAsState(
        targetValue = if (selected) expandedSize else defaultSize,
        animationSpec = tween(durationMillis = 300) // Duration for the animation
    )

    Box(
        modifier = Modifier
            .size(size) // Apply animated size
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(Color.Transparent)
    ) {
        val iconPainter = painterResource(id = iconId)
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription,
            modifier = Modifier
                .align(Alignment.Center)
                .size(size) // Apply animated size
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