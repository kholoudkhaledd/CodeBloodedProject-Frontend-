package com.example.myapplication.Teamscheduescreen


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.calander.CustomCalendar
import com.example.myapplication.calander.Indication
import com.example.myapplication.calander.getCurrentDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Month
data class TeamsCalendarResponse(
    val calendar: Map<String, String> // Adjusted to match the JSON structure
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamsScheduleScreen(context: Context) {
    var namesList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedName by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var isButtonClicked by remember { mutableStateOf(false) }

    // State for managing work-from-home and work-from-office days
    var firstTwoWeeksDays by remember { mutableStateOf<Int?>(null) }
    var secondTwoWeeksDays by remember { mutableStateOf<Int?>(null) }
    var validationMessage by remember { mutableStateOf<String?>(null) }
    val textColor = if (selectedName != null) Color.Black else Color(0xFFBDBDBD)

    val sortedNamesList = remember(namesList) { namesList.sorted() } // Sort names whenever namesList changes

    LaunchedEffect(Unit) {
        RetrofitClient.apiService.getAllUsernames().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    namesList = response.body() ?: emptyList()
                } else {
                    // Handle the error case
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Handle the failure case
                isLoading = false
            }
        })
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(32.dp)), // Adjust padding as needed
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .background(Color.White)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Team Schedule",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        textAlign = TextAlign.Center
                    )
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 16.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(10.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(Color(0xFFF6F6F6))
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { expanded = true }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                        .align(Alignment.Center)
                                ) {
                                    Text(
                                        text = selectedName ?: "Select team member",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = textColor,
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.icondropdown),
                                        contentDescription = "Dropdown Icon",
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .background(Color(0xFFF6F6F6))
                                    .height(250.dp)
                                    .width(340.dp)
                            ) {
                                sortedNamesList.forEach { name ->
                                    DropdownMenuItem(
                                        text = { Text(name, color = Color.Black) },
                                        onClick = {
                                            selectedName = name
                                            expanded = false
                                        },
                                        modifier = Modifier
                                            .background(Color(0xFFF6F6F6))
                                            .border(0.5.dp, Color(0xFFE8E8E8))
                                            .padding(vertical = 10.dp, horizontal = 16.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (selectedName != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                ) {
                    CalendarPerEmployee(context, selectedName!!)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
                    .padding(start = 30.dp, end = 30.dp, top = 16.dp, bottom = 16.dp )

            ) {
                Text(
                    text = "Change team's schedule",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(10.dp))
                        .background(Color(0xFFF6F6F6))
                        .height(50.dp)
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    WorkDaysInput(
                        value = firstTwoWeeksDays,
                        onValueChange = {
                            firstTwoWeeksDays = it
                            validationMessage = null
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(10.dp))
                        .background(Color(0xFFF6F6F6))
                        .height(50.dp)
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    WorkDaysInput(
                        value = secondTwoWeeksDays,
                        onValueChange = {
                            secondTwoWeeksDays = it
                            validationMessage = null
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (firstTwoWeeksDays != null && secondTwoWeeksDays != null) {
                            val totalDays =
                                (firstTwoWeeksDays ?: 0) + (secondTwoWeeksDays ?: 0)
                            if (totalDays != 0) {
                                // Handle the valid case, e.g., submit the data
                                println("Valid schedule: $firstTwoWeeksDays days for 2 weeks, $secondTwoWeeksDays days for other 2 weeks")
                                changeTeamSchedule(
                                    firstTwoWeeksDays!!,
                                    secondTwoWeeksDays!!
                                )

                                isButtonClicked = true
                                firstTwoWeeksDays = null
                                secondTwoWeeksDays = null

                            }
                        } else {
                            validationMessage = "enter numbers for both patterns"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isButtonClicked) Color.Gray else Color(0xFF76B31B)
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                ) {
                    Text(text = "Submit", fontSize = 16.sp)
                }
            }

            validationMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }


        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManagerCalendar(
    currentMonth: Month,
    currentYear: Int,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    context: Context,
    userName: String
) {
    CustomCalendar(
        currentMonth = currentMonth,
        currentYear = currentYear,
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
        context = context,
        userName = userName,
        isManager = true
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarViewScreenManager(context: Context,selectedName: String) {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().month) }
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
    ) {
        val currentMonthAndYear = "${currentMonth.name.lowercase().capitalize()} $currentYear"

        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(25.dp))
        ) {
            Divider(
                color = Color.LightGray,
                thickness = 0.5.dp,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(horizontal = 25.dp, vertical = 6.dp)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
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
                    .clip(RoundedCornerShape(25.dp))
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                ManagerCalendar(
                    currentMonth = currentMonth,
                    currentYear = currentYear,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                    },
                    context = context,
                    userName = selectedName
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