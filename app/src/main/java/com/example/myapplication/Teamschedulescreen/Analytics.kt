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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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
                    Text(
                        text = "Analytics",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        textAlign = TextAlign.Center
                    )



                    if (isLoading) {
                        CircularProgressIndicator() // Display loading spinner
                    } else {
                        mostRequestedDayData?.let { data ->
                            CustomPieChart(data = data.mapValues { it.value.toFloat() })
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        mostActiveProjectsData?.let { data ->
                            BarChartCard(title = "Requests Per Project", data = data)
                        } ?: run {
                                CircularProgressIndicator() // Display loading spinner

                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
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
    val spacing = 12.dp

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

        data.forEach { (project, count) ->
            val barHeight = (barMaxHeight * (count.toFloat() / maxCount)).roundToInt().coerceAtLeast(10)

            // Draw the bar
            drawRect(
                color = Color(0xFF76B31B),
                topLeft = androidx.compose.ui.geometry.Offset(xOffset, canvasHeight - barHeight - 40f),
                size = androidx.compose.ui.geometry.Size(barWidth.toPx(), barHeight.toFloat())
            )

            // Draw the request count above the bar
            drawIntoCanvas { canvas ->
                val paint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 16.sp.toPx()
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                canvas.nativeCanvas.drawText(
                    count.toString(), // Display the count
                    xOffset + barWidth.toPx() / 2,
                    canvasHeight - barHeight - 50f, // Position above the bar
                    paint
                )
            }

            // Draw the project name below the bar
            drawIntoCanvas { canvas ->
                val paint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 14.sp.toPx()
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                canvas.nativeCanvas.drawText(
                    project,
                    xOffset + barWidth.toPx() / 2,
                    canvasHeight - 5f,
                    paint
                )
            }

            xOffset += barWidth.toPx() + barSpacing
        }
    }
}



@Composable
fun CustomPieChart(data: Map<String, Float>) {
    val entries = data.map { PieEntry(it.value, it.key) }

    // Generate a unique color for each slice
    val colors = listOf(
        Color(0xFF0F9D58).toArgb(),
        Color(0xFFFFEB3B).toArgb(),
        Color(0xFFF44336).toArgb(),
        Color(0xFF4CAF50).toArgb(),
        Color(0xFF3F51B5).toArgb()
    )

    val dataSet = PieDataSet(entries, "").apply {
        sliceSpace = 3f
        selectionShift = 5f
        setColors(colors)
    }

    val pieData = PieData(dataSet).apply {
        setValueFormatter(PercentFormatter())
        setValueTextSize(15f)
        setValueTextColor(android.graphics.Color.WHITE)
        setValueTypeface(android.graphics.Typeface.DEFAULT_BOLD)
    }

    Text(
        text = "Distribution of Days Swapped From",
        fontSize = 18.sp,
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



