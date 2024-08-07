import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
fun createRequest(changeDayFrom: LocalDate, changeDayTo: LocalDate) {
    val client = OkHttpClient()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val currentDateTime = LocalDateTime.now().format(dateTimeFormatter)

    val jsonObject = JSONObject().apply {
        put("changeDayFrom", changeDayFrom.toString())
        put("changeDayTo", changeDayTo.toString())
        put("Status", "Pending")
        put("Time Stamp", currentDateTime)
    }
    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val requestBody: RequestBody = jsonObject.toString().toRequestBody(mediaType)
    val request = Request.Builder()
        .url("http://10.0.2.2:8000/create_request") // Use 10.0.2.2 to access localhost from emulator
        .post(requestBody)
        .build()

    println("Sending request with data: $jsonObject")

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            e.printStackTrace()
            println("Request failed: ${e.message}")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {
                println("Request successfully created")
                println("Response: ${response.body?.string()}")
            } else {
                println("Error: ${response.code}")
                println("Response: ${response.body?.string()}")
            }
        }
    })
}

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
                    onClick = {
                        if (selectedDate != null && changeDate != null) {
                            createRequest(selectedDate!!, changeDate!!)
                        } else {
                            println("Selected date or change date is null")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor  = if (selectedDate != null && changeDate != null) colorResource(
                        id = R.color.deloitteGreen
                    ) else colorResource(id = R.color.coolGray6)),
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

