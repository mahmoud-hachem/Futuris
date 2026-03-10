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
import com.example.futuris.R
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.components.FuturisField
import com.example.futuris.components.FuturisPasswordField
import com.example.futuris.ui.theme.LinkPurple
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite
import androidx.compose.ui.graphics.Color

@Composable
fun SignupScreen(onGoLogin: () -> Unit) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(34.dp))

            Image(
                painter = painterResource(id = R.drawable.futuris_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(95.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Create account",
                color = TitleWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sign up to get started",
                color = SoftText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        FuturisField(
                            value = firstName,
                            onValueChange = {
                                firstName = it
                                firstNameError = ""
                            },
                            placeholder = "First name",
                            height = 44
                        )
                        if (firstNameError.isNotEmpty()) {
                            ErrorText(firstNameError)
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        FuturisField(
                            value = lastName,
                            onValueChange = {
                                lastName = it
                                lastNameError = ""
                            },
                            placeholder = "Last name",
                            height = 44
                        )
                        if (lastNameError.isNotEmpty()) {
                            ErrorText(lastNameError)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        FuturisField(
                            value = dateOfBirth,
                            onValueChange = {
                                dateOfBirth = it
                                dateError = ""
                            },
                            placeholder = "DD/MM/YYYY",
                            height = 44
                        )
                        if (dateError.isNotEmpty()) {
                            ErrorText(dateError)
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    Column {
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
                                    ) {
                                        genderExpanded = true
                                        genderError = ""
                                    }
                            )

                            DropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Male") },
                                    onClick = {
                                        gender = "Male"
                                        genderExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Female") },
                                    onClick = {
                                        gender = "Female"
                                        genderExpanded = false
                                    }
                                )
                            }
                        }

                        if (genderError.isNotEmpty()) {
                            ErrorText(genderError)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                FuturisField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = ""
                    },
                    placeholder = "Username"
                )
                if (usernameError.isNotEmpty()) {
                    ErrorText(usernameError)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                FuturisField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = ""
                    },
                    placeholder = "Email address"
                )
                if (emailError.isNotEmpty()) {
                    ErrorText(emailError)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
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
                if (passwordError.isNotEmpty()) {
                    ErrorText(passwordError)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
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
                if (confirmPasswordError.isNotEmpty()) {
                    ErrorText(confirmPasswordError)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            FuturisButton(
                text = "Create account",
                onClick = {
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
                        dateError = "Use DD/MM/YYYY"
                        isValid = false
                    }

                    if (gender != "Male" && gender != "Female") {
                        genderError = "Choose Male or Female"
                        isValid = false
                    }

                    if (username.isBlank()) {
                        usernameError = "Username is required"
                        isValid = false
                    }

                    if (!isValidEmail(email)) {
                        emailError = "Invalid email format"
                        isValid = false
                    }

                    if (password.length < 6) {
                        passwordError = "Minimum 6 characters"
                        isValid = false
                    }

                    if (confirmPassword != password) {
                        confirmPasswordError = "Passwords do not match"
                        isValid = false
                    }

                    if (isValid) {
                        // TODO:
                        // 1) send data to backend
                        // 2) backend checks unique username
                        // 3) backend checks unique email
                        // 4) create account if everything is valid
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "Already have an account? ",
                    color = SoftText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Log in",
                    color = LinkPurple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onGoLogin() }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = Color(0xFFFF6B6B),
        fontSize = 12.sp,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

fun isLettersOnly(text: String): Boolean {
    return text.isNotBlank() && text.all { it.isLetter() || it.isWhitespace() }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidDate(date: String): Boolean {
    val regex = Regex("""\d{2}/\d{2}/\d{4}""")
    if (!regex.matches(date)) return false

    val parts = date.split("/")
    val day = parts[0].toIntOrNull() ?: return false
    val month = parts[1].toIntOrNull() ?: return false
    val year = parts[2].toIntOrNull() ?: return false

    if (month !in 1..12) return false
    if (day !in 1..31) return false
    if (year !in 1900..2100) return false

    return true
}