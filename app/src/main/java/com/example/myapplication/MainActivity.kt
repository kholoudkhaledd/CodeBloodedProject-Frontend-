package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
//         Finallayout()
                NotificationScreen()
            }
        }
    }
}
@Composable
fun MyApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.screen,
            Modifier.padding(innerPadding)
        ) {
            composable(Screens.Home.screen) { HomeScreen() }
            composable(Screens.Notification.screen) { NotificationScreen() }
            composable(Screens.Requests.screen) { RequestsScreen() }
            composable(Screens.Chatbot.screen) { ChatbotScreen() }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentDestination(navController) == Screens.Home.screen,
            onClick = { navController.navigate(Screens.Home.screen) }
        )
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.ic_notification), contentDescription = "Notification") },
            label = { Text("Notification") },
            selected = currentDestination(navController) == Screens.Notification.screen,
            onClick = { navController.navigate(Screens.Notification.screen) }
        )
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.ic_requests), contentDescription = "Requests") },
            label = { Text("Requests") },
            selected = currentDestination(navController) == Screens.Requests.screen,
            onClick = { navController.navigate(Screens.Requests.screen) }
        )
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.ic_chatbot), contentDescription = "Chatbot") },
            label = { Text("Chatbot") },
            selected = currentDestination(navController) == Screens.Chatbot.screen,
            onClick = { navController.navigate(Screens.Chatbot.screen) }
        )
    }
}

@Composable
fun currentDestination(navController: NavController): String? {
    return navController.currentBackStackEntry?.destination?.route
}


