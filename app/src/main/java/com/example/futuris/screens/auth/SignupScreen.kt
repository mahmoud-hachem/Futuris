package com.example.futuris.screens.auth

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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.backend.RegisterRequest
import com.example.futuris.backend.RetrofitClient
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.components.FuturisField
import com.example.futuris.components.FuturisPasswordField
import com.example.futuris.ui.theme.LinkPurple
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(onGoLogin: () -> Unit) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var genderExpanded by remember { mutableStateOf(false) }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf("") }
    var genderError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    FuturisBackground {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.futuris_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Create account",
                    color = TitleWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Sign up to get started",
                    color = SoftText,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        FuturisField(
                            value = firstName,
                            onValueChange = {
                                if (!isLoading) {
                                    firstName = it
                                    firstNameError = ""
                                }
                            },
                            placeholder = "First name",
                            height = 44
                        )
                        if (firstNameError.isNotEmpty()) ErrorText(firstNameError)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        FuturisField(
                            value = lastName,
                            onValueChange = {
                                if (!isLoading) {
                                    lastName = it
                                    lastNameError = ""
                                }
                            },
                            placeholder = "Last name",
                            height = 44
                        )
                        if (lastNameError.isNotEmpty()) ErrorText(lastNameError)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        FuturisField(
                            value = dateOfBirth,
                            onValueChange = {
                                if (!isLoading) {
                                    dateOfBirth = it
                                    dateError = ""
                                }
                            },
                            placeholder = "DD/MM/YYYY",
                            height = 44
                        )
                        if (dateError.isNotEmpty()) ErrorText(dateError)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Box {
                            FuturisField(
                                value = gender,
                                onValueChange = {},
                                placeholder = "Gender",
                                height = 44
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable(
                                        enabled = !isLoading,
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { genderExpanded = true }
                            )

                            DropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false }
                            ) {
                                listOf("Male", "Female").forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            gender = it
                                            genderError = ""
                                            genderExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        if (genderError.isNotEmpty()) ErrorText(genderError)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                FuturisField(
                    value = username,
                    onValueChange = {
                        if (!isLoading) {
                            username = it
                            usernameError = ""
                        }
                    },
                    placeholder = "Username",
                    height = 48
                )
                if (usernameError.isNotEmpty()) ErrorText(usernameError)

                Spacer(modifier = Modifier.height(8.dp))

                FuturisField(
                    value = email,
                    onValueChange = {
                        if (!isLoading) {
                            email = it
                            emailError = ""
                        }
                    },
                    placeholder = "Email address",
                    height = 48
                )
                if (emailError.isNotEmpty()) ErrorText(emailError)

                Spacer(modifier = Modifier.height(8.dp))

                FuturisPasswordField(
                    value = password,
                    onValueChange = {
                        if (!isLoading) {
                            password = it
                            passwordError = ""
                        }
                    },
                    placeholder = "Password",
                    passwordVisible = showPassword,
                    onTogglePassword = {
                        if (!isLoading) showPassword = !showPassword
                    }
                )
                if (passwordError.isNotEmpty()) ErrorText(passwordError)

                Spacer(modifier = Modifier.height(8.dp))

                FuturisPasswordField(
                    value = confirmPassword,
                    onValueChange = {
                        if (!isLoading) {
                            confirmPassword = it
                            confirmPasswordError = ""
                        }
                    },
                    placeholder = "Confirm password",
                    passwordVisible = false,
                    onTogglePassword = null
                )
                if (confirmPasswordError.isNotEmpty()) ErrorText(confirmPasswordError)

                Spacer(modifier = Modifier.height(12.dp))

                FuturisButton(
                    text = if (isLoading) "Creating account..." else "Create account"
                ) {
                    if (isLoading) return@FuturisButton

                    focusManager.clearFocus()
                    var isValid = true

                    if (!isLettersOnly(firstName)) {
                        firstNameError = "Letters only"
                        isValid = false
                    }

                    if (!isLettersOnly(lastName)) {
                        lastNameError = "Letters only"
                        isValid = false
                    }

                    if (!isValidDate(dateOfBirth)) {
                        dateError = "Invalid date"
                        isValid = false
                    } else if (!isAgeValid(dateOfBirth)) {
                        dateError = "Must be 13+"
                        isValid = false
                    }

                    if (gender.isBlank()) {
                        genderError = "Required"
                        isValid = false
                    }

                    if (username.isBlank()) {
                        usernameError = "Required"
                        isValid = false
                    }

                    if (!isValidEmail(email)) {
                        emailError = "Invalid email"
                        isValid = false
                    }

                    if (!isStrongPassword(password)) {
                        passwordError = "Min 6 chars + 1 number"
                        isValid = false
                    }

                    if (confirmPassword != password) {
                        confirmPasswordError = "Passwords do not match"
                        isValid = false
                    }

                    if (isValid) {
                        isLoading = true

                        val request = RegisterRequest(
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            dateOfBirth = dateOfBirth.trim(),
                            gender = gender.trim(),
                            username = username.trim(),
                            email = email.trim(),
                            password = password
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = RetrofitClient.api.registerUser(request)

                                Handler(Looper.getMainLooper()).post {
                                    isLoading = false

                                    if (response.isSuccessful) {

                                        val prefs = context.getSharedPreferences(
                                            "FuturisPrefs",
                                            android.content.Context.MODE_PRIVATE
                                        )

                                        prefs.edit()
                                            .putString("userId", email.trim())
                                            .putString("firstName", firstName.trim())
                                            .putString("dateOfBirth", dateOfBirth.trim())
                                            .apply()

                                        Toast.makeText(
                                            context,
                                            "Account created successfully. Please log in.",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        onGoLogin()
                                    } else {
                                        val errorMsg = response.errorBody()?.string()
                                        android.util.Log.d("SIGNUP", "ERROR: $errorMsg")

                                        Toast.makeText(
                                            context,
                                            errorMsg ?: "Signup failed. Please try again.",
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
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(
                        "Already have an account? ",
                        color = SoftText,
                        fontSize = 13.sp
                    )

                    Text(
                        "Log in",
                        color = LinkPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable(
                            enabled = !isLoading
                        ) { onGoLogin() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
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
                    text = "Creating your account",
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
fun ErrorText(message: String) {
    Text(message, color = Color.Red, fontSize = 12.sp)
}

fun isLettersOnly(text: String) =
    text.isNotBlank() && text.all { it.isLetter() || it.isWhitespace() }

fun isValidEmail(email: String) =
    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isStrongPassword(password: String) =
    password.length >= 6 && password.any { it.isDigit() }

fun isValidDate(date: String): Boolean {
    val regex = Regex("""\d{2}/\d{2}/\d{4}""")
    return regex.matches(date)
}

fun isAgeValid(date: String): Boolean {
    val year = date.split("/").last().toIntOrNull() ?: return false
    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    return currentYear - year >= 13
}