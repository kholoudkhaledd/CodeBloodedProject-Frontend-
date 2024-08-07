package com.example.myapplication

import SharedViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
@Composable
fun SplashScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    LaunchedEffect(Unit) {
        delay(2500) // Adjust delay as needed
        sharedViewModel.logoSize.value = 150.dp
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
                .background(color = Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                imageModel = R.drawable.loadingsplashscreen,
                contentDescription = "Loading",
                modifier = Modifier.size(185.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Deloitte Logo",
                modifier = Modifier
                    .size(237.dp)
                    .padding(vertical = 16.dp)
                    .animateContentSize(animationSpec = tween(durationMillis = 1000))
            )
        }
    }
}

