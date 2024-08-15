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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.RetrofitClient.apiService
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val token = Sharedpreference.getUserToken(context) // Assuming you have a method in SharedViewModel to retrieve the token

    LaunchedEffect(Unit) {
        delay(2500) // Adjust delay as needed

        scope.launch {
            try {
                // Your Retrofit client
                val response = apiService.checkToken("Bearer $token")

                if (response.isSuccessful) {
                    // Token is valid, navigate to Home Screen
                    sharedViewModel.logoSize.value = 150.dp
                    navController.navigate(Screens.Home.screen) {
                        popUpTo(Screens.SplashScreen.screen) { inclusive = true }
                    }
                } else {
                    // Token is invalid or expired, navigate to Login Screen
                    sharedViewModel.logoSize.value = 150.dp
                    navController.navigate(Screens.Login.screen) {
                        popUpTo(Screens.SplashScreen.screen) { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions (e.g., network error)
                sharedViewModel.logoSize.value = 150.dp
                Sharedpreference.removeUserToken(context)
                navController.navigate(Screens.Login.screen) {

                    popUpTo(Screens.SplashScreen.screen) { inclusive = true }
                }
            }
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


