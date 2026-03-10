package com.example.futuris.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.futuris.ui.theme.BgBottom
import com.example.futuris.ui.theme.BgTop

@Composable
fun FuturisBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(BgTop, BgBottom),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 2200f)
                )
            ),
        content = content
    )
}