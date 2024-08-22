package com.example.myapplication
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign



data class ChatMessage(val message: String, val isUserMessage: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var uid = Sharedpreference.getUserId(LocalContext.current)
    // Use MutableState to hold the position dynamically
    var context=LocalContext.current
    var isManager by remember {
        mutableStateOf(Sharedpreference.getUserPosition(context).equals("Manager", ignoreCase = true))
    }

    fun sendMessage() {
        if (message.isNotBlank()) {
            // Add the user's message to the chat
            messages = messages + ChatMessage(message, true)

            // Log the current position
            Log.d("isManagerChatbot", "Manager: $isManager")

            // Launch a coroutine to handle the API call
            coroutineScope.launch {
                try {
                    val temp = message
                    message = ""

                    // Determine which API call to make based on the position
                    val response = if (isManager)
                    {
                        Log.d("enter", "Manager: $isManager")
                        uid?.let { RetrofitClient.apiService.sendMessageManager(it, temp)

                        }
                    } else {
                        uid?.let { RetrofitClient.apiService.sendMessage(it, temp) }
                    }

                    // Handle the response
                    if (response != null) {
                        if (response.isSuccessful) {
                            val botResponse = response.body()
                            val botMessage = botResponse?.message ?: "Sorry, I didn't understand that."
                            messages = messages + ChatMessage(botMessage, false)
                        } else {
                            // Handle API error
                            messages = messages + ChatMessage(
                                "Sorry, something went wrong. Please try again.",
                                false
                            )
                        }
                    }

                    // Scroll to the bottom of the chat
                    listState.animateScrollToItem(messages.size - 1)

                } catch (e: Exception) {
                    // Handle network errors or other exceptions
                    messages = messages + ChatMessage("An error occurred: ${e.message}", false)
                }
            }
        }
    }


    val iconRes = if (message.isNotBlank()) R.drawable.send2 else R.drawable.send

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .padding(vertical = 20.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Text(
                    text = "Chatbot",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp)
                )

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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .height(54.dp)
                ) {
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        singleLine = true,
                        placeholder = { Text(text = "Message here...",
                            fontSize = 16.sp,
                            color = Color(0xFFBDBDBD),
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                        )},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(24.dp))
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Enter) {
                                    sendMessage()
                                    true
                                } else {
                                    false
                                }
                            },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = { sendMessage() },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = "Send",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                    )


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
            .padding(vertical = 10.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUserMessage) Color(0xFF5DB075) else Color(0xFFF6F6F6),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    color = if (isUserMessage) Color(0xFF5DB075) else Color(0xFFE8E8E8),
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