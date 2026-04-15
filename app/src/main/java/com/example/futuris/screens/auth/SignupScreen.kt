package com.example.futuris.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.futuris.R
import com.example.futuris.backend.RegisterRequest
import com.example.futuris.backend.RetrofitClient
import com.example.futuris.components.*
import com.example.futuris.ui.theme.*
import kotlinx.coroutines.launch
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars

@Composable
fun SignupScreen(onGoLogin: () -> Unit) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // ===== USER DATA =====
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // ===== DROPDOWNS =====
    var genderExpanded by remember { mutableStateOf(false) }

    // ===== ERRORS =====
    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf("") }
    var genderError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    FuturisBackground {
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

            // ===== NAME =====
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    FuturisField(
                        value = firstName,
                        onValueChange = {
                            firstName = it
                            firstNameError = ""
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
                            lastName = it
                            lastNameError = ""
                        },
                        placeholder = "Last name",
                        height = 44
                    )
                    if (lastNameError.isNotEmpty()) ErrorText(lastNameError)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ===== DOB + GENDER =====
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    FuturisField(
                        value = dateOfBirth,
                        onValueChange = {
                            dateOfBirth = it
                            dateError = ""
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

            // ===== USERNAME =====
            FuturisField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = ""
                },
                placeholder = "Username",
                height = 48
            )
            if (usernameError.isNotEmpty()) ErrorText(usernameError)

            Spacer(modifier = Modifier.height(8.dp))

            // ===== EMAIL =====
            FuturisField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                placeholder = "Email address",
                height = 48
            )
            if (emailError.isNotEmpty()) ErrorText(emailError)

            Spacer(modifier = Modifier.height(8.dp))

            // ===== PASSWORD =====
            FuturisPasswordField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                placeholder = "Password",
                passwordVisible = showPassword,
                onTogglePassword = { showPassword = !showPassword }
            )
            if (passwordError.isNotEmpty()) ErrorText(passwordError)

            Spacer(modifier = Modifier.height(8.dp))

            FuturisPasswordField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = ""
                },
                placeholder = "Confirm password",
                passwordVisible = false,
                onTogglePassword = null
            )
            if (confirmPasswordError.isNotEmpty()) ErrorText(confirmPasswordError)

            Spacer(modifier = Modifier.height(12.dp))

            // ===== SUBMIT =====
            FuturisButton("Create account") {
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
                    val request = RegisterRequest(
                        firstName = firstName,
                        lastName = lastName,
                        dateOfBirth = dateOfBirth,
                        gender = gender,
                        username = username,
                        email = email,
                        password = password
                    )

                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        try {
                            val response = RetrofitClient.api.registerUser(request)

                            Handler(Looper.getMainLooper()).post {
                                if (response.isSuccessful) {
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
                                        "Signup failed. Please try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            Handler(Looper.getMainLooper()).post {
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
                Text("Already have an account? ", color = SoftText, fontSize = 13.sp)
                Text(
                    "Log in",
                    color = LinkPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onGoLogin() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
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