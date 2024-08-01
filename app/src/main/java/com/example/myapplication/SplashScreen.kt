package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000) // Simulate loading time
        navController.navigate(Screens.Login.screen) {
            popUpTo(Screens.SplashScreen.screen) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.Black)
                .padding(vertical = 150.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            // Display the loading GIF using Coil's AsyncImage
            AsyncImage(
                model = R.drawable.loading, // Reference your GIF resource
                contentDescription = "Loading",
                modifier = Modifier.size(150.dp) // Adjust the size as needed
            )

            Image(
                painter = painterResource(id = R.drawable.deloittewhitelogo),
                contentDescription = "Deloitte Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(vertical = 16.dp)

//                    .offset(y = 100.dp)
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = NavController(LocalContext.current))
}
