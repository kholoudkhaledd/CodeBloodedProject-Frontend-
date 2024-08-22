@file:Suppress("PackageDirectoryMismatch")

package com.example.myapplication.manager

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.OfficeCapacityResponse
import com.example.myapplication.RetrofitClient
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt
import com.github.mikephil.charting.animation.Easing

@Composable
fun AnalyticsScreen(context: android.content.Context) {
    var mostRequestedDayData by remember { mutableStateOf<Map<String, Int>?>(null) }
    var mostActiveProjectsData by remember { mutableStateOf<Map<String, Int>?>(null) }
    var isLoading by remember { mutableStateOf(true) } // State to handle loading
    var todaysOfficeCapacity by remember { mutableStateOf<Int?>(null) }
    var mostSwappedDay by remember { mutableStateOf<String?>(null) }

    // Fetch data for most swapped day
    LaunchedEffect(Unit) {
        val call = RetrofitClient.apiService.getMostSwappedDay()
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    mostSwappedDay = response.body()?.get("day")
                } else {
                    println("API Error: ${response.code()} - ${response.message()}")
                }
                isLoading = false
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                println("API Failure: ${t.message}")
                mostSwappedDay = null
                isLoading = false
            }
        })
    }


    // Load data for pie chart (days of the week)
    LaunchedEffect(Unit) {
        val call = RetrofitClient.apiService.getMostRequestedDay()
        call.enqueue(object : Callback<Map<String, Int>> {
            override fun onResponse(
                call: Call<Map<String, Int>>,
                response: Response<Map<String, Int>>
            ) {
                if (response.isSuccessful) {
                    mostRequestedDayData = response.body()
                } else {
                    println("API Error: ${response.code()} - ${response.message()}")
                }
                isLoading = false // Set loading to false after the response
            }

            override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
                println("API Failure: ${t.message}")
                mostRequestedDayData = mapOf("Error" to 0)
                isLoading = false // Set loading to false after the failure
            }
        })
    }

    LaunchedEffect(Unit) {
        val call = RetrofitClient.apiService.getTodaysOfficeCapacity()
        call.enqueue(object : Callback<OfficeCapacityResponse> {
            override fun onResponse(
                call: Call<OfficeCapacityResponse>,
                response: Response<OfficeCapacityResponse>
            ) {
                if (response.isSuccessful) {
                    todaysOfficeCapacity = response.body()?.office_capacity
                } else {
                    println("API Error: ${response.code()} - ${response.message()}")
                }
                isLoading = false // Set loading to false after the response
            }

            override fun onFailure(call: Call<OfficeCapacityResponse>, t: Throwable) {
                println("API Failure: ${t.message}")
                todaysOfficeCapacity = null // Handle failure case
                isLoading = false // Set loading to false after the failure
            }
        })
    }




    // Load data for bar chart (projects count)
    LaunchedEffect(Unit) {
        val call = RetrofitClient.apiService.getMostActiveProjects()
        call.enqueue(object : Callback<Map<String, Int>> {
            override fun onResponse(
                call: Call<Map<String, Int>>,
                response: Response<Map<String, Int>>
            ) {
                if (response.isSuccessful) {
                    mostActiveProjectsData = response.body()
                } else {
                    println("API Error: ${response.code()} - ${response.message()}")
                }
                isLoading = false // Set loading to false after the response
            }

            override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
                println("API Failure: ${t.message}")
                mostActiveProjectsData = mapOf("Error" to 0)
                isLoading = false // Set loading to false after the failure
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
                    .clip(RoundedCornerShape(32.dp)),
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
                        text = "Analytics",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )

                    Divider(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .alpha(0.5f)
                    )

                    if (isLoading) {
                        CircularProgressIndicator() // Display loading spinner
                    } else {
                        mostRequestedDayData?.let { data ->
                            CustomPieChart(data = data.mapValues { it.value.toFloat() })
                        }

                    }


                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Card 1 - Today's Office Capacity
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .aspectRatio(0.9f),  // Ensures the card is square
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center ,

                    ) {
                        Text(text = "Today's\nCapacity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = todaysOfficeCapacity?.toString() ?: "",
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF76B31B)
                        )
                    }
                }

                // Card 2
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .aspectRatio(0.9f),  // Ensures the card is square
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = com.example.myapplication.R.drawable.retreat),
                            tint = Color(0xFF76B31B),
                            contentDescription = "Team Icon",
                            modifier = Modifier.size(90.dp)

                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .aspectRatio(0.9f),  // Ensures the card is square
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Most Swapped",
                            fontWeight = FontWeight.Bold,
                            fontSize =18.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = mostSwappedDay ?: "",
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF76B31B)
                        )
                    }
                }
            }
        }


        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(32.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .background(Color.White)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (isLoading) {
                        CircularProgressIndicator() // Display loading spinner
                    } else {

                        mostActiveProjectsData?.let { data ->
                            BarChartCard(title = "Requests Per Project", data = data)
                        } ?: run {
                            CircularProgressIndicator() // Display loading spinner

                        }
                    }
                }
            }
        }





    }
}





@Composable
fun BarChartCard(title: String, data: Map<String, Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            BarChart(data = data)
        }
    }
}


@Composable
fun BarChart(data: Map<String, Int>) {
    val maxCount = data.values.maxOrNull() ?: 1
    val barWidth = 40.dp
    val spacing = 23.dp  // Increased spacing
    val numberTextSize = 18.sp // Increase the size of the numbers on top of the bars
    val projectNameTextSize = 14.sp
    val projectNameSpacing = 12f // Space between the bar and project name

    // Same colors as the pie chart
    val colors = listOf(
        Color(0xFF74C69D),  // A soft green
        Color(0xFF52B69A),  // A slightly darker green
        Color(0xFF40916C),  // A deep green
        Color(0xFF4EA8DE),  // A calm blue
        Color(0xFF0077B6)   // A darker blue
    )

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(horizontal = 16.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val barSpacing = spacing.toPx()
        val barMaxHeight = canvasHeight * 0.6f
        val totalBarsWidth = data.size * barWidth.toPx() + (data.size - 1) * barSpacing
        val startX = (canvasWidth - totalBarsWidth) / 2f

        var xOffset = startX
        var colorIndex = 0  // Track color index to cycle through colors

        data.forEach { (project, count) ->
            val barHeight = (barMaxHeight * (count.toFloat() / maxCount)).roundToInt().coerceAtLeast(10)

            // Draw the rounded bar with a matching color
            drawRoundRect(
                color = colors[colorIndex % colors.size],  // Cycle through colors
                topLeft = androidx.compose.ui.geometry.Offset(xOffset, canvasHeight - barHeight - 40f),
                size = androidx.compose.ui.geometry.Size(barWidth.toPx(), barHeight.toFloat()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)  // Rounded corners
            )

            // Draw the request count above the bar
            drawIntoCanvas { canvas ->
                val paint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = numberTextSize.toPx()  // Increase text size
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                canvas.nativeCanvas.drawText(
                    count.toString(), // Display the count
                    xOffset + barWidth.toPx() / 2,
                    canvasHeight - barHeight - 55f, // Position above the bar with a little more spacing
                    paint
                )
            }

            // Draw the project name below the bar
            drawIntoCanvas { canvas ->
                val paint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = projectNameTextSize.toPx()
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                canvas.nativeCanvas.drawText(
                    project,
                    xOffset + barWidth.toPx() / 2,
                    canvasHeight - 5f + projectNameSpacing, // Increase spacing below the bar
                    paint
                )
            }

            xOffset += barWidth.toPx() + barSpacing
            colorIndex++  // Move to the next color
        }
    }
}






@Composable
fun CustomPieChart(data: Map<String, Float>) {
    val entries = data.map { PieEntry(it.value, it.key) }

    val colors = listOf(
        Color(0xFF74C69D).toArgb(),  // A soft green
        Color(0xFF52B69A).toArgb(),  // A slightly darker green
        Color(0xFF40916C).toArgb(),  // A deep green
        Color(0xFF4EA8DE).toArgb(),  // A calm blue
        Color(0xFF0077B6).toArgb()   // A darker blue
    )

    val dataSet = PieDataSet(entries, "").apply {
        sliceSpace = 3f
        selectionShift = 5f
        setColors(colors)
        valueTextSize = 40f  // Attempt to increase the text size for the values on the pie chart
        valueTextColor = android.graphics.Color.WHITE
    }

    val pieData = PieData(dataSet).apply {
        setValueFormatter(object : PercentFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%" // Display the number as an integer with a percentage symbol
            }

        })
        setValueTextSize(40f)  // Ensure this matches the valueTextSize above
        setValueTextColor(android.graphics.Color.WHITE)
        setValueTypeface(android.graphics.Typeface.DEFAULT_BOLD)
    }

    Text(
        text = "Distribution of Days Swapped From",
        fontSize = 20.sp,  // Increased title size
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                this.data = pieData
                setUsePercentValues(true)
                description.isEnabled = false
                isRotationEnabled = true
                setHoleColor(android.graphics.Color.WHITE)
                setTransparentCircleColor(android.graphics.Color.WHITE)
                setTransparentCircleAlpha(110)
                setHoleRadius(58f)
                setTransparentCircleRadius(61f)
                setDrawCenterText(true)
                centerText = "Days Swapped"
                setDrawEntryLabels(false)

                // Enable and customize the legend
                legend.isEnabled = true
                legend.form = com.github.mikephil.charting.components.Legend.LegendForm.CIRCLE
                legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
                legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT
                legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
                legend.textSize = 14f  // Increase the legend text size
                legend.xOffset = -12f // Adjust the offset to move it further to the right

                animateY(1400, Easing.EaseInOutQuad)
                invalidate()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}





