package com.example.futuris.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.ui.theme.CardBottom
import com.example.futuris.ui.theme.CardTop
import com.example.futuris.ui.theme.HintText
import com.example.futuris.ui.theme.TitleWhite

@Composable
fun FuturisField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    height: Int = 54
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .shadow(10.dp, RoundedCornerShape(15.dp), clip = false)
            .clip(RoundedCornerShape(15.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CardTop, CardBottom)
                )
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = TextStyle(
                color = TitleWhite,
                fontSize = 15.sp
            ),
            cursorBrush = SolidColor(TitleWhite),
            modifier = Modifier.fillMaxWidth()
        )

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = HintText,
                fontSize = 14.sp
            )
        }
    }
}