package com.example.futuris.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.components.FuturisField
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite

@Composable
fun ResetPasswordScreen(
    onContinue: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            Image(
                painter = painterResource(id = R.drawable.futuris_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(110.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Reset password",
                color = TitleWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enter your email to receive a reset link",
                color = SoftText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(44.dp))

            FuturisField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email address"
            )

            Spacer(modifier = Modifier.height(22.dp))

            FuturisButton(
                text = "Continue",
                onClick = onContinue
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Back to Log in",
                color = SoftText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onBackToLogin() }
            )
        }
    }
}