package com.example.myapplication.Requests

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.S)
fun timeAgo(timestamp: String): String {

    // Assuming timestamp is in "yyyy-MM-dd HH:mm:ss" format
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
    val requestTime = Instant.from(formatter.parse(timestamp))
    val now = Instant.now()
    val duration = Duration.between(requestTime, now)
    val seconds = duration.toSeconds()

    return when {
        seconds < 60 -> "$seconds sec ago"
        seconds < 3600 -> "${seconds / 60}m ago"
        seconds < 86400 -> "${seconds / 3600} hours ago"
        seconds < 604800 -> "${seconds / 86400} days ago"
        seconds < 2419200 -> "${seconds / 604800} weeks ago"
        else -> "${seconds / 2419200} months ago"
    }
}
