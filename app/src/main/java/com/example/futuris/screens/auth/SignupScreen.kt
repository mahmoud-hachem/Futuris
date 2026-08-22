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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import java.util.Calendar

@Composable
fun SignupScreen(
    onGoLogin: () -> Unit,
    onGoEmailVerification: () -> Unit
) {
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
    var showConfirmPassword by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var showBirthDateDialog by remember { mutableStateOf(false) }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf("") }
    var genderError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    fun openDatePicker() {
        if (!isLoading) showBirthDateDialog = true
    }

    FuturisBackground {
        Box(modifier = Modifier.fillMaxSize()) {
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
                                    firstName = it.trimStart()
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
                                    lastName = it.trimStart()
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
                        Box {
                            FuturisField(
                                value = dateOfBirth,
                                onValueChange = {},
                                placeholder = "Date of birth",
                                height = 44
                            )

                            Text(
                                text = "✦",
                                color = Color(0xFFCAA8FF),
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 12.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable(
                                        enabled = !isLoading,
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { openDatePicker() }
                            )
                        }

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

                            Text(
                                text = "⌄",
                                color = SoftText,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 12.dp)
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
                            username = it.trim().lowercase()
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
                            email = it.trim()
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
                    passwordVisible = showConfirmPassword,
                    onTogglePassword = {
                        if (!isLoading) showConfirmPassword = !showConfirmPassword
                    }
                )
                if (confirmPasswordError.isNotEmpty()) ErrorText(confirmPasswordError)

                Spacer(modifier = Modifier.height(12.dp))

                FuturisButton(
                    text = if (isLoading) "Creating account..." else "Create account"
                ) {
                    if (isLoading) return@FuturisButton

                    focusManager.clearFocus()
                    var isValid = true

                    if (!isValidCapitalizedName(firstName)) {
                        firstNameError = "Start with capital letter"
                        isValid = false
                    }

                    if (!isValidCapitalizedName(lastName)) {
                        lastNameError = "Start with capital letter"
                        isValid = false
                    }

                    if (dateOfBirth.isBlank()) {
                        dateError = "Choose your birth date"
                        isValid = false
                    } else if (!isAgeValid(dateOfBirth)) {
                        dateError = "Must be 13+"
                        isValid = false
                    }

                    if (gender.isBlank()) {
                        genderError = "Required"
                        isValid = false
                    }

                    if (!isValidUsername(username)) {
                        usernameError = "3-20 chars, start with letter"
                        isValid = false
                    }

                    if (!isValidEmail(email)) {
                        emailError = "Invalid email"
                        isValid = false
                    }

                    if (!isStrongPassword(password)) {
                        passwordError = "Min 6 chars and 1 number"
                        isValid = false
                    }

                    if (confirmPassword != password) {
                        confirmPasswordError = "Passwords do not match"
                        isValid = false
                    }

                    if (isValid) {
                        isLoading = true

                        val cleanEmail = email.trim().lowercase()

                        val request = RegisterRequest(
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            dateOfBirth = dateOfBirth.trim(),
                            gender = gender.trim(),
                            username = username.trim(),
                            email = cleanEmail,
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
                                            .putString("pendingVerificationEmail", cleanEmail)
                                            .putString("pendingVerificationFirstName", firstName.trim())
                                            .apply()

                                        Toast.makeText(
                                            context,
                                            "Account created. Verification code sent to your email.",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        onGoEmailVerification()
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

            if (showBirthDateDialog) {
                ProfessionalBirthDateDialog(
                    currentDate = dateOfBirth,
                    onDismiss = { showBirthDateDialog = false },
                    onDateSelected = {
                        dateOfBirth = it
                        dateError = ""
                        showBirthDateDialog = false
                    }
                )
            }

            FuturisPredictionLoadingOverlay(isVisible = isLoading)
        }
    }
}

@Composable
private fun ProfessionalBirthDateDialog(
    currentDate: String,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val today = Calendar.getInstance()
    val maxAllowedYear = today.get(Calendar.YEAR) - 13
    val years = (maxAllowedYear downTo 1920).toList()

    var selectedDay by remember { mutableIntStateOf(1) }
    var selectedMonth by remember { mutableIntStateOf(0) }
    var selectedYear by remember { mutableIntStateOf(maxAllowedYear - 5) }

    var monthExpanded by remember { mutableStateOf(false) }
    var dayExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }

    val currentParts = currentDate.split("/")
    if (currentParts.size == 3) {
        currentParts[0].toIntOrNull()?.let { selectedDay = it }
        currentParts[1].toIntOrNull()?.let { selectedMonth = it - 1 }
        currentParts[2].toIntOrNull()?.let { selectedYear = it }
    }

    val maxDays = daysInMonth(selectedMonth, selectedYear)
    if (selectedDay > maxDays) selectedDay = maxDays

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF271447),
                            Color(0xFF130B24),
                            Color(0xFF090411)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFCAA8FF),
                            Color(0x665B2EA6),
                            Color(0x33FFFFFF)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFFEAD9FF),
                                    Color(0xFF9C6BFF),
                                    Color(0xFF4B237A)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✦",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Choose your birth date",
                    color = TitleWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "You must be at least 13 years old to continue",
                    color = SoftText,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfessionalDateDropdown(
                        label = "Day",
                        value = selectedDay.toString().padStart(2, '0'),
                        expanded = dayExpanded,
                        onExpandedChange = { dayExpanded = it },
                        modifier = Modifier.weight(0.8f)
                    ) {
                        (1..maxDays).forEach { day ->
                            DropdownMenuItem(
                                text = {
                                    Text(day.toString().padStart(2, '0'))
                                },
                                onClick = {
                                    selectedDay = day
                                    dayExpanded = false
                                }
                            )
                        }
                    }

                    ProfessionalDateDropdown(
                        label = "Month",
                        value = months[selectedMonth],
                        expanded = monthExpanded,
                        onExpandedChange = { monthExpanded = it },
                        modifier = Modifier.weight(1.4f)
                    ) {
                        months.forEachIndexed { index, month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = index
                                    monthExpanded = false
                                }
                            )
                        }
                    }

                    ProfessionalDateDropdown(
                        label = "Year",
                        value = selectedYear.toString(),
                        expanded = yearExpanded,
                        onExpandedChange = { yearExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year.toString()) },
                                onClick = {
                                    selectedYear = year
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0x221E9BFF))
                        .border(
                            1.dp,
                            Color(0x335DA9FF),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(vertical = 12.dp, horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${selectedDay.toString().padStart(2, '0')} ${months[selectedMonth]} $selectedYear",
                        color = Color(0xFFEAD9FF),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(
                                1.dp,
                                Color(0x55FFFFFF),
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            color = SoftText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFB06CFF),
                                        Color(0xFF7C4DFF)
                                    )
                                )
                            )
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(18.dp),
                                ambientColor = Color(0xFFB06CFF),
                                spotColor = Color(0xFFB06CFF)
                            )
                            .clickable {
                                val day = selectedDay.toString().padStart(2, '0')
                                val month = (selectedMonth + 1).toString().padStart(2, '0')
                                onDateSelected("$day/$month/$selectedYear")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfessionalDateDropdown(
    label: String,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = SoftText,
            fontSize = 11.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
        )

        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x221B1233))
                    .border(
                        1.dp,
                        Color(0x44CAA8FF),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onExpandedChange(true) }
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = value,
                        color = TitleWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "⌄",
                        color = Color(0xFFCAA8FF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                content()
            }
        }
    }
}

private fun daysInMonth(month: Int, year: Int): Int {
    return when (month) {
        0, 2, 4, 6, 7, 9, 11 -> 31
        3, 5, 8, 10 -> 30
        1 -> if (isLeapYear(year)) 29 else 28
        else -> 31
    }
}

private fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

fun isValidCapitalizedName(text: String): Boolean {
    val clean = text.trim()
    return clean.length >= 2 &&
            clean.first().isUpperCase() &&
            clean.all { it.isLetter() || it == '-' || it == '\'' || it == ' ' }
}

fun isValidUsername(username: String): Boolean {
    val clean = username.trim()
    val regex = Regex("^[a-z][a-z0-9_]{2,19}$")
    return regex.matches(clean)
}

fun isValidEmail(email: String) =
    android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

fun isStrongPassword(password: String) =
    password.length >= 6 && password.any { it.isDigit() }

fun isAgeValid(date: String): Boolean {
    return try {
        val parts = date.split("/")
        val day = parts[0].toInt()
        val month = parts[1].toInt() - 1
        val year = parts[2].toInt()

        val birthDate = Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val thirteenYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -13)
        }

        !birthDate.after(thirteenYearsAgo)
    } catch (e: Exception) {
        false
    }
}