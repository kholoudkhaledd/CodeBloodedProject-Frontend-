package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.yourapp.ui.MyRequestsPage
import com.example.yourapp.ui.Request
import com.example.yourapp.ui.RequestStatus
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // Call your Firebase connection test function here
                NavigationScreen()
            }
        }
    }
}
@Composable
fun FirebaseConnection() {
    // Get a reference to the database
    val database = Firebase.database
    val myRef = database.getReference("message")

    // Test reading from the database
    myRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val data = task.result?.value
            Log.d("FirebaseConnection", "Data: $data")
        } else {
            Log.e("FirebaseConnection", "Error getting data", task.exception)
        }
    }

    // Test writing to the database
    myRef.setValue("Hello, Firebase!").addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d("FirebaseConnection", "Data written successfully")
        } else {
            Log.e("FirebaseConnection", "Error writing data", task.exception)
        }
    }
}
