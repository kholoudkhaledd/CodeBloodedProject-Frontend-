package com.example.myapplication.calander

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.greenicon
import java.time.LocalDate
import java.time.Month


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarViewScreen(context: Context) {
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
                    text = stringResource(id = R.string.work_schedule),
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
                        tint = greenicon,
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
                EmployeeCalendar(
                    currentMonth = currentMonth,
                    currentYear = currentYear,
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date },
                    context = context // Pass the context to CustomCalendar
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