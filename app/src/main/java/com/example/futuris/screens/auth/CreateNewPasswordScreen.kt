package com.example.futuris.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.components.FuturisPasswordField
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite

@Composable
fun CreateNewPasswordScreen(
    onResetDone: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showConfirmPassword by remember { mutableStateOf(false) }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
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
                text = "Create new password",
                color = TitleWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Make sure it’s strong and secure",
                color = SoftText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(42.dp))

            FuturisPasswordField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = "New password",
                passwordVisible = false,
                onTogglePassword = null
            )

            Spacer(modifier = Modifier.height(14.dp))

            FuturisPasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm password",
                passwordVisible = showConfirmPassword,
                onTogglePassword = { showConfirmPassword = !showConfirmPassword }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FuturisButton(
                text = "Reset password",
                onClick = {
                    focusManager.clearFocus()
                    onResetDone()
                }
            )
        }
    }
}