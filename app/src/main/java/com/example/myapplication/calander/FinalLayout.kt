package com.example.myapplication.calander

import RequestsSection
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Finallayout(context: Context) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
                    item {
                        // User Info Section
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFECECEC))
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                                .padding(horizontal = 25.dp)
                        ) {
                            UserInfo(context)
                        }
                    }

                    item {
                        // Display if home or office Section
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFECECEC))
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        ) {
                            Displayifhomeoroffice("Home")
                        }
                    }

                    item {
                        // Calendar View Screen
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFECECEC))
                                .fillMaxWidth()
                                .padding(0.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .height(450.dp)
                        ) {
                            CalendarViewScreen(context)
                        }
                    }

                    item {
                        // Requests Section
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFECECEC))
                                .fillMaxWidth()
                                .padding(vertical = 0.dp)
                        ) {
                            RequestsSection()
                        }
                    }
                }
            }