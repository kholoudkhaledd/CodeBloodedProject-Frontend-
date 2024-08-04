package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1000) // Simulate loading time
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
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(vertical = 150.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the loading GIF using Coil's AsyncImage
            AsyncImage(
                model = R.drawable.loadingsplashscreen, // Reference your GIF resource
                contentDescription = "Loading",
                modifier = Modifier.size(150.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.deloittewhitelogo),
                contentDescription = "Deloitte Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(vertical = 16.dp)
            )
        }
    }
}
