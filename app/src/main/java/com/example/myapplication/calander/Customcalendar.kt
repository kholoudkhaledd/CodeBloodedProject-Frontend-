package com.example.myapplication.calander

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.Sharedpreference
import com.example.myapplication.ui.theme.Darkblue
import com.example.myapplication.ui.theme.GreenJC
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Month
import java.time.Year
data class CalendarData(
    val date: String,
    val location: String
)

data class CalendarResponse(
    val calendar: Map<String, String> // Adjusted to match the JSON structure
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendar(
    modifier: Modifier = Modifier,
    currentMonth: Month,
    currentYear: Int,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    context: Context,
    userId: String? = null,
    userName: String? = null,
    isManager: Boolean = false
) {
    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
    val daysInMonth = currentMonth.length(Year.of(currentYear).isLeap)
    val startOfMonth = LocalDate.of(currentYear, currentMonth, 1)
    val startDayOfWeek = startOfMonth.dayOfWeek.value % 7
    val totalCells = (startDayOfWeek + daysInMonth + 6) / 7 * 7

    val monthNumber = currentMonth.value.toString().padStart(2, '0')
    val dateString = "$monthNumber-$currentYear"

    var calendarData by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val apiService = RetrofitClient.apiService

    LaunchedEffect(currentMonth, currentYear, userId, userName) {
        val token = ("Bearer " + Sharedpreference.getUserToken(context))
        if (token.isEmpty()) {
            // If the token is not available, return early to avoid making the API call
            Log.e(TAG, "Token is missing, cannot make API call")
            return@LaunchedEffect
        }
        val call = if (isManager && userName != null) {
            apiService.getCalendarForTeam(userName, dateString)
        } else {
            apiService.getCalendarForMonth(dateString, token)
        }

        call.enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(
                call: Call<CalendarResponse>,
                response: Response<CalendarResponse>
            ) {
                if (response.isSuccessful) {
                    calendarData = response.body()?.calendar ?: emptyMap()
                } else {
                    Log.e(TAG, "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                Log.e(TAG, "API Failure: ${t.message}")
            Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show()

            }
        })
    }

    Column(
        modifier = modifier
            .background(Color.White)
            .padding(0.dp)

    ) {
        // Header Row with Days of Week
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 4.dp)
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }

        // Calendar Days
        for (rowIndex in 0 until totalCells / 7) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .weight(0.9f)
                    .padding(2.dp)
            ) {
                for (columnIndex in 0 until 7) {
                    val dayIndex = rowIndex * 7 + columnIndex - startDayOfWeek + 1
                    if (dayIndex in 1..daysInMonth) {
                        val dayString = "${dayIndex.toString().padStart(2, '0')}-${currentMonth.name.take(3).lowercase().replaceFirstChar { it.uppercase() }}"

                        val location = calendarData[dayString]
                        val backgroundColor = when (location) {
                            "Home" -> GreenJC
                            "Office" -> Darkblue
                            else -> Color.Gray
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(1.dp)
                                .clickable { onDateSelected(dayString) }
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$dayIndex",
                                color = backgroundColor,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    val today = LocalDate.now()
    return "${today.dayOfMonth}-${today.monthValue}-${today.year}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmployeeCalendar(
    currentMonth: Month,
    currentYear: Int,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    context: Context
) {
    val userId = Sharedpreference.getUserId(context)
    CustomCalendar(
        currentMonth = currentMonth,
        currentYear = currentYear,
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
        context = context,
        userId = userId,
        isManager = false
    )
}
