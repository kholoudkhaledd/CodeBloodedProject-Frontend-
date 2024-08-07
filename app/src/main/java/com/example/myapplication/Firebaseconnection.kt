package com.example.myapplication

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.Firebase
import com.google.firebase.database.database

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