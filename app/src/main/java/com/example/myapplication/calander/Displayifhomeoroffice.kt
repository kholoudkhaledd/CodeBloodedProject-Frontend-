package com.example.myapplication.calander

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.GreenJC
// Data Class for API Response

// LABEL HOME OR OFFICE
@Composable
fun Displayifhomeoroffice(place: String) {
    val backgroundColor = GreenJC // Adjust colors as need
    Log.d("Place", place)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(8.dp)),
        color = backgroundColor,
        contentColor = Color.White
    ) {
        Text(
            text = $place",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 18.dp)
        )
    }
}