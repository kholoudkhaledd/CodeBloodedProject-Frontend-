package com.example.myapplication

import SharedViewModel
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun LoginScreen(
    navController: NavController,
    context: Context,
    sharedViewModel: SharedViewModel,
    onLoginResult: (Boolean, String) -> Unit
) {
    // Get the current activity's window
    val window = (context as Activity).window

    LaunchedEffect(Unit) {
        // Set the system UI visibility flags to hide the status and navigation bars
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    // Your existing login screen code
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isTextFieldNotEmpty by remember {
        derivedStateOf { email.isNotEmpty() && password.isNotEmpty() }
    }
    var passwordVisibility by remember { mutableStateOf(false) }

    Image(
        painter = painterResource(id = R.drawable.img),
        contentDescription = "Background",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
    )
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.deloittelogo),
            contentDescription = "Deloitte Logo",
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
        Text(
            text = "Login",
            color = Color.White,
            style = LocalTextStyle.current.copy(fontSize = 25.sp),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Image(
                        painter = painterResource(
                            id = if (passwordVisibility) R.drawable.show else R.drawable.hide
                        ),
                        contentDescription = if (passwordVisibility) "Hide" else "Show",
                        modifier = Modifier.size(25.dp),
                        colorFilter = if (password.isNotEmpty()) ColorFilter.tint(colorResource(id = R.color.deloitteGreen))
                        else ColorFilter.tint(Color.Gray)
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        Button(
            onClick = {
                if (isTextFieldNotEmpty) {
                    loginUser(email, password, navController, context, sharedViewModel) { success, position ->
                        if (success) {
                            // Navigate to home screen if login is successful
                            navController.navigate(Screens.Home.screen) {
                                popUpTo(Screens.Login.screen) { inclusive = true }
                            }
                            onLoginResult(success, position)
                            Log.d(TAG, "User position after login: $position")
                        } else {
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTextFieldNotEmpty) colorResource(id = R.color.deloitteGreen)
                else colorResource(id = R.color.coolGray6)
            )
        ) {
            Text(
                text = "Login",
                fontSize = 16.sp
            )
        }
    }
}




fun loginUser(
    email: String, password: String, navController: NavController,
    context: Context, sharedViewModel: SharedViewModel,
    onResult: (Boolean, String) -> Unit
) {
    Sharedpreference.clearAll(context)
    Log.d(TAG, "context before login: " + context)



    val loginRequest = LoginRequest(email, password) // Only include email and password
    RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                loginResponse?.let {
                    Sharedpreference.saveUserId(context, loginResponse.uid)
                    Sharedpreference.saveUserName(context, loginResponse.username)
                    Sharedpreference.saveUserToken(context, loginResponse.token_id)
                    Sharedpreference.saveUserPosition(context,loginResponse.position)
                    sharedViewModel.setUserInfo(it.uid)

                    // Get user position from the response
                    val userPosition = loginResponse.position ?: "" // Default to empty string if null

                    Log.d(TAG, "Name: " + Sharedpreference.getUserName(context))
                    Log.d(TAG, "UID: " + Sharedpreference.getUserId(context))
                    Log.d(TAG, "User Position: $userPosition") // Log the user position

                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val token = task.result
                        updateNotificationToken(it.uid, token)

                    })


                    Toast.makeText(context, "Login successful. Welcome back ${loginResponse.username}!", Toast.LENGTH_LONG).show()
                    onResult(true, userPosition)  // Pass position to onResult
                }
            } else {
                Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_LONG).show()
                onResult(false, "") // Notify that login failed
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            onResult(false, "") // Notify that login failed
        }
    })
}

fun updateNotificationToken(userId: String, notifToken: String) {
    val tokenUpdate = notifTokenModel(notifToken)

    RetrofitClient.apiService.update_notif_token(userId, tokenUpdate).enqueue(object :
        Callback<Void> {
        override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
            if (response.isSuccessful) {
                Log.d("updateNotificationToken", "Notification token updated successfully")
            } else {
                Log.e("updateNotificationToken", "Failed to update notification token: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.e("updateNotificationToken", "Error updating notification token", t)
        }
    })
}

class PasswordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = AnnotatedString("*".repeat(text.length))
        return TransformedText(transformedText, OffsetMapping.Identity)
    }
}