package com.example.myapplication.Navigationscreen

import SharedViewModel
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ChatScreen
import com.example.myapplication.LoginScreen
import com.example.myapplication.R
import com.example.myapplication.Screens
import com.example.myapplication.Sharedpreference
import com.example.myapplication.SplashScreen
import com.example.myapplication.calander.Finallayout
import com.example.myapplication.manager.ManagerRequest
import com.example.myapplication.manager.TeamsScheduleScreen
import com.example.myapplication.notifications.ui.theme.NotificationScreen
import com.example.yourapp.ui.MyRequestsPage
import android.content.Intent
import com.example.myapplication.MainActivity // Adjust the package name as needed
import android.app.Activity


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    var selectedScreen by remember { mutableStateOf(Screens.SplashScreen.screen) }
    val context = LocalContext.current

    // Using mutableStateOf to dynamically track the user's position
    var isManager by remember { mutableStateOf(Sharedpreference.getUserPosition(context).equals("Manager", ignoreCase = true)) }

    Log.d("isManagerNavbar", "Man: $isManager")

    Scaffold(
        topBar = {
            if (selectedScreen !in listOf(Screens.SplashScreen.screen, Screens.Login.screen)) {
                TopAppBar(
                    title = { Text(text = "") },
                    actions = {
                        IconButton(onClick = {
                            Sharedpreference.clearAll(context)
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                            (context as Activity).finish()
                        }) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (selectedScreen !in listOf(Screens.SplashScreen.screen, Screens.Login.screen)) {
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
                        if (isManager) {
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
                LoginScreen(navController, context, sharedViewModel) { success, position ->
                    // Handle post-login logic here if needed
                    if (success) {
                        // Update the isManager state based on the new position
                        isManager = position.equals("Manager", ignoreCase = true)
                        navController.navigate(Screens.Home.screen) {
                            popUpTo(Screens.SplashScreen.screen) { inclusive = true }
                        }
                    }
                }
            }
            composable(Screens.Home.screen) {
                selectedScreen = Screens.Home.screen
                Finallayout(context)
            }
            composable(Screens.Chatbot.screen) {
                selectedScreen = Screens.Chatbot.screen
                ChatScreen()
            }
            composable(Screens.Notification.screen) {
                selectedScreen = Screens.Notification.screen
                NotificationScreen(isManager = isManager)
            }

            composable(Screens.Requests.screen) {
                selectedScreen = Screens.Requests.screen
                if (isManager)
                    ManagerRequest()
                else
                    MyRequestsPage()
            }
            composable(Screens.TeamsSchedule.screen) {
                selectedScreen = Screens.TeamsSchedule.screen
                TeamsScheduleScreen(context)
            }
        }
    }
}


