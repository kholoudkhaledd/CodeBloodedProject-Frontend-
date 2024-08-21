package com.example.myapplication.calander

import RequestsSection
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.Sharedpreference
import com.example.myapplication.ui.theme.lightgraycolor

data class DayScheduleResponse(
    val day: String,
    val location: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Finallayout(context: Context) {
    val currentDate = getCurrentDate()
    val (location, setLocation) = remember { mutableStateOf("Loading...") }


    // Fetch data when the composable is first displayed
    LaunchedEffect(Unit) {
        try {
            val token = ("Bearer " + Sharedpreference.getUserToken(context))
            val currentDate = getCurrentDate()  // Assuming this gets the current date in "dd-MM-yyyy" format
            val (day, month, year) = currentDate.split("-").map { it.padStart(2, '0') }

            // Make the API call with token
            val response = RetrofitClient.apiService.getDaySchedule(month, day, year, token)

            setLocation(response.location)  // Update the location
        } catch (e: Exception) {
            setLocation("Error: ${e.message}")  // Handle error
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(lightgraycolor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // User Info Section
            Box(
                modifier = Modifier
                    .background(lightgraycolor)
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
                    .background(lightgraycolor)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Displayifhomeoroffice(place = location)
            }
        }

        item {
            // Calendar View Screen
            Box(
                modifier = Modifier
                    .background(lightgraycolor)
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
                    .background(lightgraycolor)
                    .fillMaxWidth()
                    .padding(vertical = 0.dp)
            ) {
                RequestsSection()
            }
        }
    }
}

