package com.example.myapplication
import SharedViewModel
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@Composable
fun LoginScreen(navController: NavController, context: Context,
                sharedViewModel: SharedViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isTextFieldNotEmpty by remember { mutableStateOf(false) }
    // var isLoginSuccessful by remember { mutableStateOf(false) }
    isTextFieldNotEmpty = email.isNotEmpty() && password.isNotEmpty()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(0.3f)
                .alpha(0.9f)
        )
        Image(
            painter = painterResource(id = R.drawable.deloittelogo),
            contentDescription = "Deloitte Logo",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
                .offset(y = 80.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-40).dp),
            shape = RoundedCornerShape(12.dp),
        )
        Text(text = "Login", modifier = Modifier.offset(y = (-130).dp), color = Color.White,
            style = LocalTextStyle.current.copy(fontSize = 25.sp)
        )
        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = 40.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordTrans(),
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
                        colorFilter = if (password.isNotEmpty()) ColorFilter.
                        tint(colorResource(id = R.color.deloitteGreen))
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
                    loginUser(email, password, navController, context, sharedViewModel) {
                        if (it) {
                            // Navigate to home screen if login is successful
                            navController.navigate(Screens.Home.screen) {
                                popUpTo(Screens.Login.screen) { inclusive = true }
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        navController.context,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(50.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-320).dp),
            colors = ButtonDefaults.
            buttonColors(containerColor = if (isTextFieldNotEmpty) colorResource(
                id = R.color.deloitteGreen
            ) else colorResource(id = R.color.coolGray6))
        ) {
            Text(
                text = "Login",
                fontSize = 16.sp
            )
        }
    }
}
fun loginUser(email: String, password: String, navController: NavController,
              context: Context, sharedViewModel: SharedViewModel,
              onResult: (Boolean) -> Unit) {
    val loginRequest = LoginRequest(email, password)
    RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                loginResponse?.let {
                    Sharedpreference.saveUserId(context, loginResponse.uid)
                    Sharedpreference.saveUserName(context,loginResponse.username)
                    sharedViewModel.setUserInfo(it.uid)
                    Log.d(TAG, "Name: " + Sharedpreference.getUserName(context))
                    Log.d(TAG, "UID: " + Sharedpreference.getUserId(context))


                }
                Toast.makeText(navController.context,
                    "Login successful: ${loginResponse?.uid}",
                    Toast.LENGTH_LONG).show()
                onResult(true)  // Notify that login was successful
            } else {
                Toast.makeText(navController.context,
                    "Login failed: ${response.message()}",
                    Toast.LENGTH_LONG).show()
                onResult(false) // Notify that login failed
            }
        }
        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(navController.context, "Error: ${t.message}",
                Toast.LENGTH_LONG).show()
            onResult(false) // This notifies me that the login failed
        }
    })
}
class PasswordTrans : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = AnnotatedString("*".repeat(text.length))
        return TransformedText(transformedText, OffsetMapping.Identity)
    }
}