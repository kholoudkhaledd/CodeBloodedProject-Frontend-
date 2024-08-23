package com.example.myapplication.Requests

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.S)
fun formatDate(dateString: String): String {
    // Define possible date formats
    val possibleDateFormats = listOf(
        "yyyy-MM-dd", // Expected format
        "dd-MM-yyyy"  // Alternate format
    )

    for (format in possibleDateFormats) {
        try {
            // Parse the date from the format
            val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
            val date = LocalDate.parse(dateString, formatter)

            // Extract the day, month, and year
            val day = date.dayOfMonth
            val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }

            // Determine the correct suffix for the day
            val suffix = when (day) {
                1, 21, 31 -> "st"
                2, 22 -> "nd"
                3, 23 -> "rd"
                else -> "th"
            }

            // Return the formatted date as "21st of August"
            return "$day$suffix of $month"
        } catch (e: Exception) {
            // If parsing fails, continue to the next format
        }
    }

    // If none of the formats work, return the original date string
    return dateString
}

