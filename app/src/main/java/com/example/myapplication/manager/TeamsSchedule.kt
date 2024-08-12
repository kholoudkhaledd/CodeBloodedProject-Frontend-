package com.example.myapplication.manager


import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.calander.Indication
import com.example.myapplication.calander.getCurrentDate
import java.time.LocalDate
import java.time.Month
import java.time.Year


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamsScheduleScreen() {
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
                    CalendarPerEmployee()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarPerEmployee() {
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
                CalendarViewScreenManager()
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendarManager(
    modifier: Modifier = Modifier,
    currentMonth: Month,
    currentYear: Int,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
) {
    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
    val daysInMonth = currentMonth.length(Year.of(currentYear).isLeap)
    val startOfMonth = LocalDate.of(currentYear, currentMonth, 1)
    val startDayOfWeek = startOfMonth.dayOfWeek.value % 7
    val totalCells = (startDayOfWeek + daysInMonth + 6) / 7 * 7

    // Format month as lowercase abbreviated string (e.g., "aug")
    val monthAbbreviation = currentMonth.name.substring(0, 3).lowercase()

    // State to hold the calendar data
    var calendarData by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // Fetch calendar data using Retrofit


    Column(
        modifier = modifier
            .background(Color.White)
            .padding(0.dp) // Remove any extra padding
    ) {
        // Header Row with Days of Week
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp), // Adjust padding to fit smaller size
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp // Adjust font size for smaller header
                )
            }
        }

        // Calendar Days
        for (rowIndex in 0 until totalCells / 7) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(43.dp)
            ) {
                for (columnIndex in 0 until 7) {
                    val dayIndex = rowIndex * 7 + columnIndex - startDayOfWeek + 1
                    if (dayIndex in 1..daysInMonth) {
                        // Adjust dateString to the format "dd-MMM"
                        val dateString = "${dayIndex.toString().padStart(2, '0')}-$monthAbbreviation"
                        val location = calendarData[dateString] ?: "Unknown"
                        Log.d(ContentValues.TAG, "CustomCalendar: Date: $dateString, Location: $location")

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f) // Ensures each cell is square
                                .padding(1.dp) // Adjust padding to make boxes smaller
                                .clickable { onDateSelected(dateString) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$dayIndex\n$location",
                                color = if (location == "Home") Color.Black else Color.Blue,
                                fontSize = 15.sp, // Adjust font size for smaller days
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f) // Empty box for padding
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp)) // Adjusted height for smaller spacing
    }
}







@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarViewScreenManager() {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().month) }
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }

    Column(
        modifier = Modifier
            .background(Color.White)
            .clip(RoundedCornerShape(25.dp))
    ) {
        val currentMonthAndYear = "${currentMonth.name.lowercase().capitalize()} $currentYear"

        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(50.dp))
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Working from home/office schedule",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(vertical = 15.dp, horizontal = 25.dp)
                )
            }

            Divider(
                color = Color.LightGray,
                thickness = 0.5.dp,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(horizontal = 25.dp, vertical = 6.dp)
            )

            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 15.dp)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous month",
                        tint = Color(0xFF76B31B),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (currentMonth == Month.JANUARY) {
                                    currentMonth = Month.DECEMBER
                                    currentYear -= 1
                                } else {
                                    currentMonth = currentMonth.minus(1)
                                }
                            }
                    )

                    Text(
                        text = currentMonthAndYear,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Next month",
                        tint = Color(0xFF76B31B),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (currentMonth == Month.DECEMBER) {
                                    currentMonth = Month.JANUARY
                                    currentYear += 1
                                } else {
                                    currentMonth = currentMonth.plus(1)
                                }
                            }
                    )
                }
            }

            // Custom calendar component
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                CustomCalendarManager(
                    modifier = Modifier.fillMaxSize(),
                    currentMonth = currentMonth,
                    currentYear = currentYear,
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date }
                    // Pass the context to CustomCalendar
                )
            }

            Divider(
                color = Color.LightGray,
                thickness = 0.5.dp,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(horizontal = 25.dp, vertical = 5.dp)
            )

            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            ) {
                Indication()
            }
        }
    }
}

