package com.example.myapplication.Navigationscreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.greenicon

@Composable
fun BarIcon(
    selected: Boolean,
    iconId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    // Define default and expanded sizes
    val defaultSize = 50.dp
    val expandedSize = 64.dp

    // Animate size change
    val size by animateDpAsState(
        targetValue = if (selected) expandedSize else defaultSize,
        animationSpec = tween(durationMillis = 300) // Duration for the animation
    )

    Box(
        modifier = Modifier
            .size(size) // Apply animated size
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(Color.Transparent)
    ) {
        val iconPainter = painterResource(id = iconId)
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription,
            modifier = Modifier
                .align(Alignment.Center)
                .size(size) // Apply animated size
                .padding(bottom = 7.dp),
            tint = if (selected) Color(0xFF76B31B) else Color(0xFF8F8EA2)
        )

        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(5.dp)
                    .background(greenicon, shape = CircleShape)
            )
        }
    }
}