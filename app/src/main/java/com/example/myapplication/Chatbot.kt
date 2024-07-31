package com.example.myapplication.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chatbot() {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
    ) {
        // Chatbot header
        Text(
            text = "Chatbot",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Chat messages
        ChatMessage(
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis.",
            isUserMessage = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChatMessage(
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            isUserMessage = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChatMessage(
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            isUserMessage = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChatMessage(
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis.",
            isUserMessage = false
        )

        Spacer(modifier = Modifier.weight(1f))

        // Message input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(8.dp)
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text("Message here...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                onClick = { /* Handle send message */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF4CAF50), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChatMessage(message: String, isUserMessage: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUserMessage) Color(0xFF4CAF50) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = if (isUserMessage) Color.White else Color.Black
            )
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    Chatbot()
}

