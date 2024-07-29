package com.example.myapplication


import android.annotation.SuppressLint
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.ArrowForward
//import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.ui.theme.Darkblue
import com.example.myapplication.ui.theme.GrayD
import com.example.myapplication.ui.theme.GreenJC

import com.example.myapplication.ui.theme.MyApplicationTheme

import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter

// USER INFO SECTION
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserInfo(name:String) {
    // Get current date
    val currentDate = LocalDate.now()
    val currentDay = currentDate.dayOfMonth.toString()
    val currentDayName = currentDate.format(DateTimeFormatter.ofPattern("EEEE"))
    val currentMonthAndYear = "${currentDate.month} ${currentDate.year}"

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()

    ) {
        // First row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome Back",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(

                text = " $currentDay  $currentDayName",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Spacer between rows

        // Second row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = currentMonthAndYear,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun Indication(){
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(Darkblue, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "From Office",
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(35.dp))
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(GreenJC, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "From Home",
            fontSize = 20.sp
        )
    }
}


//PREVIEW
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CalendarViewScreenPreview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayD), // Set background color to debug
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // User Info Section
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .padding(top=5.dp) // Ensure no padding

        ) {
            UserInfo("Kholoud")
        }

        // Display if home or office Section
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .padding(vertical = 10.dp) // Ensure no padding
        ) {
            Displayifhomeoroffice("Home")
        }

        // Calendar View Screen
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
//                    .clip(RoundedCornerShape(40.dp))
                .weight(1f) // Allows the CalendarViewScreen to expand and take up available space
                .padding(0.dp) // Ensure no padding
        ) {
            CalendarViewScreen()
        }


        // Requests Section
        Box(
            modifier = Modifier
                .background(GrayD)
                .fillMaxWidth()
                .padding(vertical = 0.dp) // Ensure no padding
                .weight(0.6f)
        ) {
            RequestsSection()
        }
    }
}


//LABEL HOME OR OFFICE
@Composable
fun Displayifhomeoroffice( place: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth() // Make the Surface take the full width of the parent
            .padding(horizontal = 20.dp) // Add padding to the left and right of the Surface
            .clip(RoundedCornerShape(10.dp)), // Apply rounded corners
        color = GreenJC, // Set background color
        contentColor = Color.White // Set content color (for text)
    ) {
        Text(
            text = "Today you are working from $place",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 10.dp) // Add padding around the text inside the Surface
        )
    }
}
////Calander
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendar(
    modifier: Modifier = Modifier,
    currentMonth: Month,
    currentYear: Int,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
    val daysInMonth = currentMonth.length(Year.of(currentYear).isLeap)
    val startOfMonth = LocalDate.of(currentYear, currentMonth, 1)
    val startDayOfWeek = startOfMonth.dayOfWeek.value % 7
    val totalCells = (startDayOfWeek + daysInMonth + 6) / 7 * 7

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
            ) {
                for (columnIndex in 0 until 7) {
                    val dayIndex = rowIndex * 7 + columnIndex - startDayOfWeek + 1
                    if (dayIndex in 1..daysInMonth) {
                        val dateString = "$dayIndex-${currentMonth.value}-$currentYear"
                        val isSelected = dateString == selectedDate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f) // Ensures each cell is square
                                .padding(1.dp) // Adjust padding to make boxes smaller
                                .clickable { onDateSelected(dateString) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayIndex.toString(),
                                color = if (isSelected) Color.Blue else Color.Black,
                                fontSize = 15.sp // Adjust font size for smaller days
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
fun CalendarViewScreen() {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().month) }
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }

    Column(
        modifier = Modifier
            .background(GrayD)

    ) {
        val currentMonthAndYear = "${currentMonth.name} $currentYear"

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding( vertical = 10.dp)
                .background(GrayD)
                .clip(RoundedCornerShape(30.dp))
        ) {
            Box(modifier = Modifier.background(Color.White).fillMaxWidth()) {
                Text(
                    text = "Working from home/office schedule",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 12.dp)
                )

            }

            Box(modifier = Modifier.background(Color.White).fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous month",
                        tint = GreenJC,
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
                        tint = GreenJC,
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
            ) {
                CustomCalendar(
                    modifier = Modifier.fillMaxSize(),
                    currentMonth = currentMonth,
                    currentYear = currentYear,
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date }
                )
            }

            Divider(
                color = Color.Gray,
                thickness = 0.9.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .padding(vertical = 0.dp) // Ensure no padding
            ) {
                Indication()
            }
        }
    }
}





@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    val today = LocalDate.now()
    return "${today.dayOfMonth}-${today.monthValue}-${today.year}"
}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Finallayout() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayD), // Set background color to debug
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // User Info Section
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .padding(top = 5.dp) // Ensure no padding

        ) {
            UserInfo("Kholoud")
        }

        // Display if home or office Section
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .padding(vertical = 10.dp) // Ensure no padding
        ) {
            Displayifhomeoroffice("Home")
        }

        // Calendar View Screen
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
//                    .clip(RoundedCornerShape(40.dp))
                .weight(1f) // Allows the CalendarViewScreen to expand and take up available space
                .padding(0.dp) // Ensure no padding
        ) {
            CalendarViewScreen()
        }


        // Requests Section
        Box(
            modifier = Modifier
                .background(GrayD)
                .fillMaxWidth()
                .padding(vertical = 0.dp) // Ensure no padding
                .weight(0.6f)
        ) {
            RequestsSection()
        }
    }
}







@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestsSection() {
    // Get the current date and day
    val currentDate = LocalDate.now()
    val currentDay = currentDate.dayOfMonth

    // Create a list of days greater than the current day
    val daysList = (currentDay + 1..31).toList()
    val daysList2 = (currentDay + 1..31).toList()

    // State to manage the selected day and expanded state of dropdown
    var selectedDay by remember { mutableStateOf<Int?>(null) } // Use nullable type for initial placeholder
    var changeday by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
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
                text = "Schedule change requests",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            )

            // Selected Day
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .wrapContentSize(Alignment.TopStart)
                    .clip(RoundedCornerShape(30.dp))

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GrayD)
                        .clip(RoundedCornerShape(20.dp)) // Set the corner radius here
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable { expanded = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectedDay?.toString() ?: "select day",
                            color = Color.Black,
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.select),
                            contentDescription = "Dropdown Icon",
                            Modifier.size(20.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    daysList.forEach { day ->
                        DropdownMenuItem(
                            text = { Text(day.toString(), color = Color.Black) },
                            onClick = {
                                selectedDay = day
                                expanded = false
                            },
                            modifier = Modifier.background(Color.Gray)
                        )
                    }
                }
            }

            // Changed Day
            // Second row with a dropdown box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .wrapContentSize(Alignment.TopStart)
                    .clip(RoundedCornerShape(30.dp))

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GrayD)
                        .clip(RoundedCornerShape(8.dp)) // Set the corner radius here
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable { expanded2 = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = changeday?.toString() ?: "change to",
                            color = Color.Black,
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.select),
                            contentDescription = "Dropdown Icon",
                            Modifier.size(20.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    daysList2.forEach { day ->
                        DropdownMenuItem(
                            text = { Text(day.toString(), color = Color.Black) },
                            onClick = {
                                changeday = day
                                expanded2 = false
                            },
                            modifier = Modifier.background(Color.Gray)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SimplifiedCalendarPreview() {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        CalendarViewScreen()

    }
}
