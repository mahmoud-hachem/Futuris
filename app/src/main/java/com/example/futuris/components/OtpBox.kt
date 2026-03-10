package com.example.futuris.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.ui.theme.CardBottom
import com.example.futuris.ui.theme.CardTop
import com.example.futuris.ui.theme.TitleWhite

@Composable
fun OtpBox(
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CardTop, CardBottom)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= 1 && (it.isEmpty() || it.all { ch -> ch.isDigit() })) {
                    onValueChange(it)
                }
            },
            singleLine = true,
            textStyle = TextStyle(
                color = TitleWhite,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            cursorBrush = SolidColor(TitleWhite)
        )

        if (value.isEmpty()) {
            Text(
                text = "",
                color = TitleWhite
            )
        }
    }
}