package com.example.myapplication.calander

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import java.time.LocalDate
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestsSection() {
    // Get the current date and day
    val currentDate = LocalDate.now()
    val currentDay = currentDate.dayOfMonth

    // State to manage the selected date
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var changeDate by remember { mutableStateOf<LocalDate?>(null) }

    // State to manage the visibility of the date picker dialogs
    var showDatePicker by remember { mutableStateOf(false) }
    var showChangeDatePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(25.dp))
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 15.dp, horizontal = 25.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            // First row with text at the start
            Text(
                text = "Schedule change requests",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            )

            // Selected Date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .wrapContentSize(Alignment.TopStart)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFECECEC))
                        .clip(RoundedCornerShape(20.dp))
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable { showDatePicker = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectedDate?.toString() ?: "Select date",
                            color = Color.Gray,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp, start = 8.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "Calendar Icon",
                            Modifier.size(26.dp)
                        )
                    }
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDateSelected = { date ->
                            selectedDate = date
                            showDatePicker = false
                        },
                        onDismissRequest = { showDatePicker = false }
                    )
                }
            }

            // Change Date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .wrapContentSize(Alignment.TopStart)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFECECEC))
                        .clip(RoundedCornerShape(20.dp))
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable { showChangeDatePicker = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = changeDate?.toString() ?: "Change to",
                            color = Color.Gray,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp, start = 8.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "Calendar Icon",
                            Modifier.size(26.dp)
                        )
                    }
                }

                if (showChangeDatePicker) {
                    DatePickerDialog(
                        onDateSelected = { date ->
                            changeDate = date
                            showChangeDatePicker = false
                        },
                        onDismissRequest = { showChangeDatePicker = false }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Button(
                    onClick = { /* Handle submit action */ },
                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        setOnDismissListener { onDismissRequest() }
    }.show()
}
