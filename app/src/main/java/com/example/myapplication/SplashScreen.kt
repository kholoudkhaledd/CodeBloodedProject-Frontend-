package com.example.myapplication

import SharedViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.RetrofitClient.apiService
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SplashScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val token = Sharedpreference.getUserToken(context) // Assuming you have a method in SharedViewModel to retrieve the token

    LaunchedEffect(Unit) {
        delay(1000)
        startAnimation = true
        delay(1000)

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
                Sharedpreference.removeUserPosition(context)
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
                modifier = Modifier.size(185.dp).padding(top = 10.dp)
            )

            AnimatedLogo(startAnimation = startAnimation)
        }
    }
}

@Composable
fun AnimatedLogo(startAnimation: Boolean) {
    val offsetY by animateDpAsState(
        targetValue = if (startAnimation) (-310).dp else -10.dp,
        animationSpec = tween(durationMillis = 1000), // Set the duration for smooth transition
        label = "LogoAnimation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = offsetY)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Deloitte Logo",
            modifier = Modifier.size(230.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    // Providing a dummy NavController and SharedViewModel for preview purposes
    val dummyNavController = rememberNavController()
    val dummySharedViewModel = SharedViewModel()

    SplashScreen(navController = dummyNavController, sharedViewModel = dummySharedViewModel)
}

@Preview(showBackground = true)
@Composable
fun PreviewAnimatedLogo() {
    // Previewing the AnimatedLogo with the startAnimation flag as true
    AnimatedLogo(startAnimation = true)
}