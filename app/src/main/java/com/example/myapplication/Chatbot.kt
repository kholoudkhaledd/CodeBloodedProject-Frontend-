package com.example.myapplication.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class ChatMessage(val message: String, val isUserMessage: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun sendMessage() {
        if (message.isNotBlank()) {
            messages = messages + ChatMessage(message, true)
            messages = messages + ChatMessage("Hello, I am your ChatBot. How can I assist you today?", false)
            message = ""
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

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
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .padding(bottom = 8.dp)
                ) {
                    items(messages) { chatMessage ->
                        ChatMessageItem(
                            message = chatMessage.message,
                            isUserMessage = chatMessage.isUserMessage
                        )
                    }
                }

                // Message input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(50.dp))
                        .padding(0.5.dp)
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
                        onClick = { sendMessage() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF4EA362), shape = CircleShape)
                            .align(Alignment.CenterVertically)
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
fun ChatMessageItem(message: String, isUserMessage: Boolean) {
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
