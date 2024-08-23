
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.Retrofit.CreateRequest
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.Sharedpreference
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.darkerlightgrey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestsSection() {
    val context = LocalContext.current
    val userId = Sharedpreference.getUserId(context) ?: return

    // Get the current date and day
    val currentDate = LocalDate.now()
    val currentDay = currentDate.dayOfMonth

    // State to manage the selected date
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var changeDate by remember { mutableStateOf<LocalDate?>(null) }

    // State to manage the visibility of the date picker dialogs
    var showDatePicker by remember { mutableStateOf(false) }
    var showChangeDatePicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun isWeekend(date: LocalDate?): Boolean { return date?.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY) }
    fun isValidDate(date: LocalDate?): Boolean { return date != null && !date.isBefore(currentDate) && !isWeekend(date) }

    Box(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(32.dp))
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 15.dp, horizontal = 25.dp)
                .background(Color.White)
        ) {
            // First row with text at the start
            Text(
                text = stringResource(id = R.string.Schedule_change_requests),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            )

            // Selected Date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .wrapContentSize(Alignment.TopStart)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, LightGray, RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF6F6F6))
                        .clip(RoundedCornerShape(20.dp))
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable { showDatePicker = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectedDate?.toString() ?: stringResource(id = R.string.Select_day),
                            fontSize = 14.sp,
                            color = darkerlightgrey,
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
                    .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF6F6F6))
                        .clip(RoundedCornerShape(20.dp))
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable { showChangeDatePicker = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = changeDate?.toString() ?: stringResource(id = R.string.Change_to),
                            fontSize = 14.sp,
                            color = Color(0xFFBDBDBD),
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
                    .fillMaxWidth() .
                    wrapContentSize(Alignment.TopStart)
            ) {
                Button(
                    onClick = {
                        errorMessage = when {
                            selectedDate == null || changeDate == null -> "Selected date or change date is null"
                            !isValidDate(selectedDate) || !isValidDate(changeDate) -> "Please make sure that neither date is invalid (in the past or on a weekend)."
                            else -> {
                                createRequest(userId, selectedDate!!, changeDate!!)
                                selectedDate = null
                                changeDate = null
                                null
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isValidDate(selectedDate) && isValidDate(changeDate)) {
                            colorResource(id = R.color.deloitteGreen)
                        } else {
                            colorResource(id = R.color.coolGray6)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.Submit), fontSize = 16.sp)
                }
            }
            // Display error message below the submit button
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun createRequest(userId: String, changeDayFrom: LocalDate, changeDayTo: LocalDate) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val currentDateTime = LocalDateTime.now().format(dateTimeFormatter)

    val createRequest = CreateRequest(
        userID = userId,
        changeDayFrom = changeDayFrom.toString(),
        changeDayTo = changeDayTo.toString(),
        Status = "Pending",
        timeStamp = currentDateTime
    )

    RetrofitClient.apiService.createRequest(userId, createRequest).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                println("Request successfully created")
            } else {
                println("Error: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            t.printStackTrace()
            println("Request failed: ${t.message}")
        }
    })
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
