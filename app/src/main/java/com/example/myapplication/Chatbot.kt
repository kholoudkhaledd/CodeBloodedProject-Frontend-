import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import kotlinx.coroutines.launch

data class ChatMessage(val message: String, val isUserMessage: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    fun sendMessage() {
        if (message.isNotBlank()) {
            messages = messages + ChatMessage(message, true)
            messages = messages + ChatMessage("Hello, I am your ChatBot. How can I assist you today?", false)
            message = ""
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
            keyboardController?.hide() // Dismiss the keyboard
        }
    }

    // Determine which icon to use based on whether there is text in the message
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
                    fontSize = 24.sp,
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
                        .height(50.dp)
                ) {
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        placeholder = { Text("Message here...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(24.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = { sendMessage() },
                                modifier = Modifier
                                    .size(36.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = "Send",
                                    tint = Color.Unspecified, // No tint, use the PNG's original color
                                    modifier = Modifier
                                        .size(36.dp)
                                )
                            }
                        }
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
