package com.example.futuris.screens.auth

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.backend.ResendVerificationCodeRequest
import com.example.futuris.backend.RetrofitClient
import com.example.futuris.backend.VerifyEmailRequest
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.ui.theme.CardBottom
import com.example.futuris.ui.theme.CardTop
import com.example.futuris.ui.theme.LinkPurple
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EmailVerificationScreen(
    onVerified: () -> Unit,
    onGoLogin: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("FuturisPrefs", Context.MODE_PRIVATE)
    }

    val pendingEmail = remember {
        prefs.getString("pendingVerificationEmail", "") ?: ""
    }

    val code = remember {
        mutableStateListOf("", "", "", "", "", "")
    }

    val focusRequesters = remember {
        List(6) { FocusRequester() }
    }

    var isLoading by remember { mutableStateOf(false) }

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
                text = if (pendingEmail.isNotBlank()) {
                    "We sent a 6-digit code to\n$pendingEmail"
                } else {
                    "We sent a 6-digit code to\nyour email"
                },
                color = SoftText,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(44.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..5) {
                    AutoMoveOtpBox(
                        value = code[i],
                        enabled = !isLoading,
                        focusRequester = focusRequesters[i],
                        onValueChange = { newValue ->
                            val digit = newValue.filter { it.isDigit() }.take(1)

                            code[i] = digit

                            if (digit.isNotEmpty() && i < 5) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            FuturisButton(
                text = if (isLoading) "Verifying..." else "Verify",
                onClick = {
                    if (isLoading) return@FuturisButton

                    val finalCode = code.joinToString("").trim()

                    if (pendingEmail.isBlank()) {
                        Toast.makeText(
                            context,
                            "Missing email to verify. Please sign up again.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@FuturisButton
                    }

                    if (finalCode.length != 6) {
                        Toast.makeText(
                            context,
                            "Please enter the 6-digit code",
                            Toast.LENGTH_LONG
                        ).show()
                        return@FuturisButton
                    }

                    isLoading = true

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitClient.api.verifyEmail(
                                VerifyEmailRequest(
                                    email = pendingEmail,
                                    code = finalCode
                                )
                            )

                            Handler(Looper.getMainLooper()).post {
                                isLoading = false

                                if (response.isSuccessful) {
                                    prefs.edit()
                                        .remove("pendingVerificationEmail")
                                        .remove("pendingVerificationFirstName")
                                        .apply()

                                    Toast.makeText(
                                        context,
                                        "Email verified successfully. You can now log in.",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    onVerified()
                                } else {
                                    val errorMsg = response.errorBody()?.string()

                                    Toast.makeText(
                                        context,
                                        errorMsg ?: "Verification failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            Handler(Looper.getMainLooper()).post {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Network error: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row {
                Text(
                    text = "Didn't get a code? ",
                    color = SoftText,
                    fontSize = 13.sp
                )

                Text(
                    text = if (isLoading) "Please wait..." else "Resend",
                    color = LinkPurple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(
                        enabled = !isLoading,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (pendingEmail.isBlank()) {
                            Toast.makeText(
                                context,
                                "Missing email to resend code.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@clickable
                        }

                        isLoading = true

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = RetrofitClient.api.resendVerificationCode(
                                    ResendVerificationCodeRequest(
                                        email = pendingEmail
                                    )
                                )

                                Handler(Looper.getMainLooper()).post {
                                    isLoading = false

                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Verification code resent",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        val errorMsg = response.errorBody()?.string()

                                        Toast.makeText(
                                            context,
                                            errorMsg ?: "Could not resend code",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } catch (e: Exception) {
                                Handler(Looper.getMainLooper()).post {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Network error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Back to login",
                color = LinkPurple,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onGoLogin() }
            )
        }
    }
}

@Composable
private fun AutoMoveOtpBox(
    value: String,
    enabled: Boolean,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 43.dp, height = 54.dp)
            .focusRequester(focusRequester)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CardTop, CardBottom)
                ),
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (enabled) onValueChange(it)
            },
            enabled = enabled,
            singleLine = true,
            textStyle = TextStyle(
                color = TitleWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            cursorBrush = SolidColor(TitleWhite),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}