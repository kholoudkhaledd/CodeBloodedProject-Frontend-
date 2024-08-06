package com.example.myapplication.calander

import android.os.Build
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Month
import java.time.Year

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
                    .height(43.dp)
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