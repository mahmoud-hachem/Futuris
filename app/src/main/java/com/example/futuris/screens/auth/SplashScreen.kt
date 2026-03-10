package com.example.futuris.screens.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.components.FuturisBackground
import com.example.futuris.ui.theme.SoftText
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    var activeDot by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(8) {
            delay(300)
            activeDot = (activeDot + 1) % 3
        }
        onDone()
    }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(150.dp))

            Image(
                painter = painterResource(id = R.drawable.futuris_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(170.dp)
            )

            Spacer(modifier = Modifier.height(85.dp))

            Text(
                text = "Analyzing your future...",
                color = SoftText.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(120.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val alpha by animateFloatAsState(
                        targetValue = if (index == activeDot) 1f else 0.4f,
                        label = "dotAlpha"
                    )

                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .alpha(alpha)
                            .clip(CircleShape)
                            .background(androidx.compose.ui.graphics.Color.White)
                    )
                }
            }
        }
    }
}