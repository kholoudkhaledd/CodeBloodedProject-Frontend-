package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField


import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.MyApplicationTheme


@Composable
fun LoginScreen(navController: NavController) {
    // State variables for email and password
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isTextFieldNotEmpty by remember { mutableStateOf(false) }

    isTextFieldNotEmpty = email.isNotEmpty() && password.isNotEmpty()


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {

        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(0.3f)
                .alpha(0.9f)
        )

        Image(
            painter = painterResource(id = R.drawable.deloittewhitelogo),
            contentDescription = "Deloitte Logo",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
                .offset(y = 100.dp)
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
                .offset(y = -40.dp),shape = RoundedCornerShape(12.dp),
        )

        Text(text = "Login", modifier = Modifier.offset(y=(-130).dp ),color = Color.White ,
            style = LocalTextStyle.current.copy(fontSize = 25.sp)
        )

        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        TextField(
            value = password,
            onValueChange = {password=it},
            singleLine = true,
            maxLines = 1,
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = 40.dp),
            visualTransformation =  if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon =  {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    if (passwordVisibility)
                    {
                        Image(painterResource(id = R.drawable.show), "show", modifier = Modifier.size(25.dp))
                    }
                    else
                    {
                        Image(painterResource(id = R.drawable.hide), "hide", modifier = Modifier.size(25.dp))
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

            )


        Button(
            onClick = { /* Need Still to edit it */
                navController.navigate(Screens.Home.screen) {
                    popUpTo(Screens.Login.screen) { inclusive = true }
                }

            },
            modifier = Modifier
                .width(350.dp)
                .height(50.dp)
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-280).dp),


            colors = ButtonDefaults.buttonColors(containerColor  = if (isTextFieldNotEmpty) colorResource(
                id = R.color.deloitteGreen
            ) else colorResource(id = R.color.coolGray6))

        ) {
            Text(text = "Login")
        }


    }
}