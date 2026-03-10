package com.example.futuris.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberUpdatedState
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
import com.example.futuris.components.OtpBox
import com.example.futuris.ui.theme.LinkPurple
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite

@Composable
fun EmailVerificationScreen(
    onVerify: () -> Unit,
    onResend: () -> Unit
) {
    val code = remember {
        mutableStateListOf("", "", "", "", "", "")
    }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 34.dp),
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
                text = "Check your email",
                color = TitleWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "We sent a 6-digit code to your email",
                color = SoftText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(44.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..5) {
                    OtpBox(
                        value = code[i],
                        onValueChange = { code[i] = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            FuturisButton(
                text = "Verify",
                onClick = onVerify
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row {
                Text(
                    text = "Didn't get a code? ",
                    color = SoftText,
                    fontSize = 13.sp
                )

                Text(
                    text = "Resend",
                    color = LinkPurple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onResend() }
                )
            }
        }
    }
}