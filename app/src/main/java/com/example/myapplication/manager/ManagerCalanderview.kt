package com.example.myapplication.manager


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.calander.CalendarViewScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Viewteamscalander() {
    // List of names
    val namesList = listOf("Alice", "Bob", "Charlie", "Diana")

    // State to manage the selected name and expanded state of dropdown
    var selectedName by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(20.dp))
//            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        ) {

            // First row with text at the start
            Text(
                text = "Select a name",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            )

            // Dropdown for selecting names
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .wrapContentSize(Alignment.TopStart)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable { expanded = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectedName ?: "Select team member ",
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    namesList.forEach { name ->
                        DropdownMenuItem(
                            text = { Text(name, color = Color.Black) },
                            onClick = {
                                selectedName = name
                                expanded = false
                            },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }
            }

            // Box displaying the selected name
            if (selectedName != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))

                ) {
                    Calanderperemployee()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calanderperemployee() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().padding(vertical = 10.dp)
            .background(Color(0xFFECECEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        item {
            // Display if home or office Section
            Box(
                modifier = Modifier
                    .background(Color(0xFFECECEC))
                    .fillMaxWidth()
                    .padding(vertical = 20.dp , horizontal = 20.dp)
            ) {
                Text("Click on day to change between office&home")
            }
        }

        item {
            // Calendar View Screen
            Box(
                modifier = Modifier
                    .background(Color(0xFFECECEC))
                    .fillMaxWidth()
                    .padding(0.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .height(450.dp)
            ) {
                CalendarViewScreen()
            }
        }
        item {    Button(
            onClick = { /* Handle submit action */ },
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Text("Save")
        } }

    }
}

