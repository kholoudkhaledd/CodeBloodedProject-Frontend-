package com.example.myapplication.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(dateString: String): String {
        // Adjust the formatter to match 'yyyy-MM-dd' format
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            val date = LocalDate.parse(dateString, formatter)

            // Desired format: 'dd of MMMM'
            val desiredFormatter = DateTimeFormatter.ofPattern("dd 'of' MMMM")

            date.format(desiredFormatter)
        } catch (e: DateTimeParseException) {
            // Handle parsing error if any
            dateString // Return the original date string if parsing fails
        }
    }
}
