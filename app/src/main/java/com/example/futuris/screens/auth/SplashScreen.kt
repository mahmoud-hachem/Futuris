package com.example.futuris.screens.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.components.FuturisBackground
import com.example.futuris.ui.theme.ButtonRight
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    val loadingTexts = listOf(
        "Reading your destiny...",
        "Analyzing your future...",
        "Unlocking new possibilities..."
    )

    var currentTextIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(4) {
            delay(1200)
            currentTextIndex = (currentTextIndex + 1) % loadingTexts.size
        }
    }

    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds
        onDone()
    }

    FuturisBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(170.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(145.dp),
                    strokeWidth = 5.dp,
                    color = ButtonRight,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )

                Image(
                    painter = painterResource(id = R.drawable.futuris_logo),
                    contentDescription = "Futuris logo",
                    modifier = Modifier
                        .size(110.dp) // FULL size inside the loader
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Launching Futuris",
                color = TitleWhite,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Crossfade(targetState = loadingTexts[currentTextIndex], label = "loadingText") { text ->
                Text(
                    text = text,
                    color = SoftText.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}