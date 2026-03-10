package com.example.futuris.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.components.FuturisField
import com.example.futuris.ui.theme.CardBottom
import com.example.futuris.ui.theme.CardTop
import com.example.futuris.ui.theme.HintText
import com.example.futuris.ui.theme.LinkPurple
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite

@Composable
fun LoginScreen(
    onGoSignup: () -> Unit,
    onForgotPassword: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            Image(
                painter = painterResource(id = R.drawable.futuris_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(135.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Welcome back",
                color = TitleWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sign in to continue",
                color = SoftText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(55.dp))

            FuturisField(
                value = username,
                onValueChange = { username = it },
                placeholder = "Username"
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordWithForgotField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                onForgotClick = onForgotPassword
            )

            Spacer(modifier = Modifier.height(30.dp))

            FuturisButton(
                text = "Log In",
                onClick = { focusManager.clearFocus() }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row {
                Text(
                    text = "Don’t have an account? ",
                    color = SoftText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Sign up",
                    color = LinkPurple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onGoSignup() }
                )
            }
        }
    }
}

@Composable
private fun PasswordWithForgotField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onForgotClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
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
            singleLine = true,
            textStyle = TextStyle(
                color = TitleWhite,
                fontSize = 15.sp
            ),
            cursorBrush = SolidColor(TitleWhite),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 105.dp)
        )

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = HintText,
                fontSize = 14.sp
            )
        }

        Text(
            text = "Forgot password?",
            color = LinkPurple,
            fontSize = 12.sp,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onForgotClick() }
                .align(Alignment.CenterEnd)
        )
    }
}