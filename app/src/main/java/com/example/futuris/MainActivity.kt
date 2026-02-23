package com.example.futuris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FuturisApp() }
    }
}

/* ---------- Theme Colors (matching your design) ---------- */
private val BgTop = Color(0xFF0B1B4B)
private val BgBottom = Color(0xFF071234)
private val Card = Color(0xFF273A86)      // input/card color
private val Card2 = Color(0xFF1F2D6E)     // slightly darker
private val TextWhite = Color(0xFFF2F5FF)
private val TextSoft = Color(0xFFB9C6FF)
private val ButtonFill = Color(0xFF7F8CFF)
private val ButtonFill2 = Color(0xFFB6C1FF)

private enum class Screen { Splash, Login, Signup }

@Composable
fun FuturisApp() {
    var screen by remember { mutableStateOf(Screen.Splash) }

    when (screen) {
        Screen.Splash -> SplashScreen(onDone = { screen = Screen.Login })
        Screen.Login -> LoginScreen(
            onGoSignup = { screen = Screen.Signup }
        )
        Screen.Signup -> SignupScreen(
            onGoLogin = { screen = Screen.Login }
        )
    }
}

/* -------------------- Background -------------------- */
@Composable
private fun FuturisBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(BgTop, BgBottom),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 2200f)
                )
            ),
        content = content
    )
}

/* -------------------- Splash -------------------- */
@Composable
private fun SplashScreen(onDone: () -> Unit) {
    var dot by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // animate dots while waiting
        repeat(8) {
            delay(300)
            dot = (dot + 1) % 3
        }
        onDone()
    }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            GlowLogo(modifier = Modifier.size(170.dp))

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Analyzing your future...",
                color = TextSoft,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) { i ->
                    val a by animateFloatAsState(if (i == dot) 1f else 0.35f, label = "dot")
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(Color.White.copy(alpha = a))
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun GlowLogo(modifier: Modifier = Modifier) {
    // Simple glow: layered alpha + shadow
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = 0.18f }
                .shadow(35.dp, RectangleShape, clip = false)
        )
        Image(
            painter = painterResource(id = R.drawable.futuris_logo),
            contentDescription = "Futuris Logo",
            modifier = Modifier.fillMaxSize()
        )
    }
}

/* -------------------- Login -------------------- */
@Composable
private fun LoginScreen(onGoSignup: () -> Unit) {
    val focus = LocalFocusManager.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(36.dp))

            GlowLogo(modifier = Modifier.size(130.dp))

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Welcome back",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Sign in to continue",
                color = TextSoft,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(22.dp))

            FuturisField(
                value = username,
                onValueChange = { username = it },
                placeholder = "Username"
            )

            Spacer(Modifier.height(12.dp))

            FuturisField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true,
                passwordVisible = showPass,
                onTogglePassword = { showPass = !showPass }
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot password?",
                    color = TextSoft,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(18.dp))

            FuturisButton(text = "Log In") {
                focus.clearFocus()
                // TODO: connect login later
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Don't have an account? Sign up",
                color = TextSoft,
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onGoSignup() }
            )
        }
    }
}

/* -------------------- Signup -------------------- */
@Composable
private fun SignupScreen(onGoLogin: () -> Unit) {
    val focus = LocalFocusManager.current

    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var showPass2 by remember { mutableStateOf(false) }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))

            GlowLogo(modifier = Modifier.size(110.dp))

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Create account",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Sign up to get started",
                color = TextSoft,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(18.dp))

            // Two small fields row (First / Last)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    FuturisField(value = first, onValueChange = { first = it }, placeholder = "First name")
                }
                Box(Modifier.weight(1f)) {
                    FuturisField(value = last, onValueChange = { last = it }, placeholder = "Last name")
                }
            }

            Spacer(Modifier.height(12.dp))

            // Two small fields row (DOB / Gender)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    FuturisField(value = dob, onValueChange = { dob = it }, placeholder = "Date of birth")
                }
                Box(Modifier.weight(1f)) {
                    FuturisField(value = gender, onValueChange = { gender = it }, placeholder = "Gender")
                }
            }

            Spacer(Modifier.height(12.dp))

            FuturisField(value = user, onValueChange = { user = it }, placeholder = "Username")
            Spacer(Modifier.height(12.dp))
            FuturisField(value = email, onValueChange = { email = it }, placeholder = "Email address")
            Spacer(Modifier.height(12.dp))

            FuturisField(
                value = pass,
                onValueChange = { pass = it },
                placeholder = "Password",
                isPassword = true,
                passwordVisible = showPass,
                onTogglePassword = { showPass = !showPass }
            )

            Spacer(Modifier.height(12.dp))

            FuturisField(
                value = pass2,
                onValueChange = { pass2 = it },
                placeholder = "Confirm password",
                isPassword = true,
                passwordVisible = showPass2,
                onTogglePassword = { showPass2 = !showPass2 }
            )

            Spacer(Modifier.height(18.dp))

            FuturisButton(text = "Create account") {
                focus.clearFocus()
                // TODO: connect signup later
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Already have an account? Log in",
                color = TextSoft,
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onGoLogin() }
            )
        }
    }
}

/* -------------------- Reusable UI -------------------- */
@Composable
private fun FuturisField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    val visual = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(10.dp, RoundedCornerShape(14.dp), clip = false)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(listOf(Card, Card2))
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            textStyle = TextStyle(color = TextWhite, fontSize = 14.sp),
            visualTransformation = visual,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = if (isPassword) 34.dp else 0.dp)
        )

        if (value.isEmpty()) {
            Text(text = placeholder, color = TextSoft.copy(alpha = 0.8f), fontSize = 13.sp)
        }

        if (isPassword && onTogglePassword != null) {
            // Minimal “eye” using text (no icon dependency)
            Text(
                text = if (passwordVisible) "Hide" else "Show",
                color = TextSoft,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTogglePassword() }
            )
        }
    }
}

@Composable
private fun FuturisButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(18.dp, RoundedCornerShape(16.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(listOf(ButtonFill, ButtonFill2))
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}