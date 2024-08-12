package com.example.myapplication.calander

import RequestsSection
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import java.time.LocalDate

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import java.time.LocalDate
import java.time.Month



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarViewScreen() {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().month) }
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }

    Column(
        modifier = Modifier
            .background(Color.White)
            .clip(RoundedCornerShape(32.dp))
    ) {
        val currentMonthAndYear = "${currentMonth.name.lowercase().capitalize()} $currentYear"

        Column(
            modifier = Modifier
                //.fillMaxWidth()
                .padding(vertical = 10.dp)
                //.background(Color.White)
                .clip(RoundedCornerShape(50.dp))
        ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()) {
                Text(
                    text = "Working from home/office schedule",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold ,
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

            Box(modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 15.dp)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Previous month",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 13.dp)
                            .size(12.dp)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Image(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "Next month",
                        modifier = Modifier
                            .padding(start = 13.dp)
                            .align(Alignment.CenterVertically)
                            .size(12.dp)
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
                CustomCalendar(
                    modifier = Modifier.fillMaxSize(),
                    currentMonth = currentMonth,
                    currentYear = currentYear,
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date }
                )
            }

            Divider(
                color = Color(0xFFF1F1F1) ,
                thickness = 1.dp,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(horizontal = 25.dp, vertical = 5.dp)
            )



@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    val today = LocalDate.now()
    return "${today.dayOfMonth}-${today.monthValue}-${today.year}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Finallayout(context: Context) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // User Info Section
            Box(
                modifier = Modifier
                    .background(Color(0xFFECECEC))
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 25.dp)
            ) {
                UserInfo(context)
            }
        }

        item {
            // Display if home or office Section
            Box(
                modifier = Modifier
                    .background(Color(0xFFECECEC))
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Displayifhomeoroffice("Home")
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
                CalendarViewScreen(context)
            }
        }

        item {
            // Requests Section
            Box(
                modifier = Modifier
                    .background(Color(0xFFECECEC))
                    .fillMaxWidth()
                    .padding(vertical = 0.dp)
            ) {
                RequestsSection()
            }
        }
    }
}

