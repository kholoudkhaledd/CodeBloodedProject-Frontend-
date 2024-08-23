package com.example.myapplication.calander

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.Sharedpreference
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// USER INFO SECTION
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserInfo(context: Context) {
    // Get current date
    val currentDate = LocalDate.now()
    val currentDay = currentDate.dayOfMonth.toString()
    val currentDayName = currentDate.format(DateTimeFormatter.ofPattern("E")) // Short day name
    val currentMonthAndYear = "${currentDate.month.name.lowercase().capitalize().substring(0, 3)} ${currentDate.year}" // Short month name
    val username = Sharedpreference.getUserName(context) ?: stringResource(id = R.string.Guest) // Default to "Guest" if username is null

    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        // First row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.Welcome_Back),
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = "$currentDay $currentDayName",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        // Second row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = username,
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
