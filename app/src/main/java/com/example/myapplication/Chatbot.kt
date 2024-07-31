package com.example.myapplication.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
fun ChatScreen() {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Card that encapsulates header and messages
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                // Chatbot header
                Text(
                    text = "Chatbot",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp)
                )

                // Chat messages
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    ChatMessage(
                        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis.",
                        isUserMessage = false
                    )
                    ChatMessage(
                        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                        isUserMessage = true
                    )
                    ChatMessage(
                        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                        isUserMessage = true
                    )
                    ChatMessage(
                        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fringilla quam eu facilisis mollis.",
                        isUserMessage = false
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Message input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(24.dp))
                        .padding(8.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
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
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    IconButton(
                        onClick = { /* Handle send message */ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF4EA362), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessage(message: String, isUserMessage: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUserMessage) Color(0xFF4EA362) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
                .widthIn(max = 250.dp),
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
    ChatScreen()
}
