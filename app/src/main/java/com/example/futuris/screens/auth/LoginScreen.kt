package com.example.futuris.screens.auth

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.backend.LoginRequest
import com.example.futuris.backend.RetrofitClient
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.components.FuturisField
import com.example.futuris.ui.theme.CardBottom
import com.example.futuris.ui.theme.CardTop
import com.example.futuris.ui.theme.HintText
import com.example.futuris.ui.theme.LinkPurple
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun LoginScreen(
    onGoSignup: () -> Unit,
    onForgotPassword: () -> Unit,
    onGoEmailVerification: () -> Unit,
    onLoginSuccess: (
        String, // username
        String, // firstName
        String, // lastName
        String, // email
        String, // dateOfBirth
        String  // gender
    ) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    FuturisBackground {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
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
                    value = email,
                    onValueChange = {
                        if (!isLoading) email = it
                    },
                    placeholder = "Email"
                )

                Spacer(modifier = Modifier.height(12.dp))

                PasswordWithForgotField(
                    value = password,
                    onValueChange = {
                        if (!isLoading) password = it
                    },
                    placeholder = "Password",
                    onForgotClick = {
                        if (!isLoading) onForgotPassword()
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))

                FuturisButton(
                    text = if (isLoading) "Logging in..." else "Log In",
                    onClick = {
                        if (isLoading) return@FuturisButton

                        focusManager.clearFocus()

                        val cleanEmail = email.trim().lowercase()
                        val cleanPassword = password.trim()

                        if (cleanEmail.isEmpty() || cleanPassword.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please fill all fields",
                                Toast.LENGTH_LONG
                            ).show()
                            return@FuturisButton
                        }

                        isLoading = true

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val request = LoginRequest(
                                    email = cleanEmail,
                                    password = cleanPassword
                                )

                                val response = RetrofitClient.api.loginUser(request)

                                Handler(Looper.getMainLooper()).post {
                                    isLoading = false

                                    if (response.isSuccessful && response.body() != null) {
                                        val user = response.body()?.user

                                        val loggedUsername = user?.username?.trim().orEmpty()
                                        val loggedFirstName = user?.firstName?.trim().orEmpty()
                                        val loggedLastName = user?.lastName?.trim().orEmpty()
                                        val loggedDateOfBirth = user?.dateOfBirth?.trim().orEmpty()
                                        val loggedGender = user?.gender?.trim().orEmpty()

                                        val safeUsername =
                                            if (loggedUsername.isNotBlank()) loggedUsername else "User"

                                        Toast.makeText(
                                            context,
                                            response.body()?.message ?: "Welcome back 👋",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        val prefs = context.getSharedPreferences(
                                            "FuturisPrefs",
                                            Context.MODE_PRIVATE
                                        )

                                        prefs.edit()
                                            .putBoolean("isLoggedIn", true)
                                            .putString("username", safeUsername)
                                            .putString("firstName", loggedFirstName)
                                            .putString("lastName", loggedLastName)
                                            .putString("email", cleanEmail)
                                            .putString("dateOfBirth", loggedDateOfBirth)
                                            .putString("gender", loggedGender)
                                            .apply()

                                        onLoginSuccess(
                                            safeUsername,
                                            loggedFirstName,
                                            loggedLastName,
                                            cleanEmail,
                                            loggedDateOfBirth,
                                            loggedGender
                                        )
                                    } else {
                                        val errorText = response.errorBody()?.string().orEmpty()

                                        val requiresVerification = try {
                                            JSONObject(errorText).optBoolean("requiresVerification", false)
                                        } catch (e: Exception) {
                                            false
                                        }

                                        val message = try {
                                            JSONObject(errorText).optString("message", "Login failed")
                                        } catch (e: Exception) {
                                            "Login failed"
                                        }

                                        if (requiresVerification) {
                                            val prefs = context.getSharedPreferences(
                                                "FuturisPrefs",
                                                Context.MODE_PRIVATE
                                            )

                                            prefs.edit()
                                                .putString("pendingVerificationEmail", cleanEmail)
                                                .apply()

                                            Toast.makeText(
                                                context,
                                                message,
                                                Toast.LENGTH_LONG
                                            ).show()

                                            onGoEmailVerification()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Invalid email or password ❌",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }

                            } catch (e: Exception) {
                                Handler(Looper.getMainLooper()).post {
                                    isLoading = false

                                    Toast.makeText(
                                        context,
                                        "Server error ⚠️",
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
                            enabled = !isLoading,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onGoSignup() }
                    )
                }
            }

            FuturisPredictionLoadingOverlay(isVisible = isLoading)
        }
    }
}

@Composable
private fun FuturisPredictionLoadingOverlay(isVisible: Boolean) {
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 250),
        label = "overlay_alpha"
    )

    if (overlayAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .alpha(overlayAlpha),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FuturisPredictionLoader()

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Reading your future...",
                    color = TitleWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Signing you in",
                    color = SoftText,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun FuturisPredictionLoader() {
    val infiniteTransition = rememberInfiniteTransition(label = "prediction_loader")

    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outer_rotation"
    )

    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1700, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "inner_rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x66C084FF),
                            Color(0x33A855F7),
                            Color.Transparent
                        )
                    )
                )
                .alpha(glowAlpha)
        )

        Canvas(
            modifier = Modifier
                .size(118.dp)
                .rotate(outerRotation)
        ) {
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFFB388FF),
                        Color(0xFF7C4DFF),
                        Color(0x33000000),
                        Color(0xFFB388FF)
                    )
                ),
                style = Stroke(width = 7f)
            )

            drawCircle(
                color = Color(0xFFCAA8FF),
                radius = 8f,
                center = Offset(size.width / 2f, 8f)
            )
        }

        Canvas(
            modifier = Modifier
                .size(82.dp)
                .rotate(innerRotation)
        ) {
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF9C6BFF),
                        Color(0x664E2A84),
                        Color(0xFF9C6BFF)
                    )
                ),
                style = Stroke(width = 5f)
            )

            drawCircle(
                color = Color(0xFFE0CCFF),
                radius = 6f,
                center = Offset(size.width / 2f, size.height - 6f)
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .scale(pulseScale)
                .shadow(
                    elevation = 18.dp,
                    shape = CircleShape,
                    ambientColor = Color(0xFFB06CFF),
                    spotColor = Color(0xFFB06CFF)
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFF3E8FF),
                            Color(0xFFB06CFF),
                            Color(0xFF5B2496)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✦",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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
            .shadow(10.dp, RoundedCornerShape(15.dp))
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
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onForgotClick() }
        )
    }
}