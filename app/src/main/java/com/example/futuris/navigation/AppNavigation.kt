package com.example.futuris.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.futuris.screens.auth.CreateNewPasswordScreen
import com.example.futuris.screens.auth.EmailVerificationScreen
import com.example.futuris.screens.auth.LoginScreen
import com.example.futuris.screens.auth.ResetPasswordScreen
import com.example.futuris.screens.auth.SignupScreen
import com.example.futuris.screens.auth.SplashScreen
import com.example.futuris.screens.home.HomeScreen

enum class Screen {
    Splash,
    Login,
    Signup,
    ResetPassword,
    EmailVerification,
    CreateNewPassword,
    Home
}

@Composable
fun FuturisApp() {
    var currentScreen by remember { mutableStateOf(Screen.Splash) }

    // temporary test value
    // later this should come from the logged-in user
    var userFirstName by remember { mutableStateOf("Alex") }

    when (currentScreen) {
        Screen.Splash -> SplashScreen(
            onDone = { currentScreen = Screen.Home }
        )

        Screen.Login -> LoginScreen(
            onGoSignup = { currentScreen = Screen.Signup },
            onForgotPassword = { currentScreen = Screen.ResetPassword }
        )

        Screen.Signup -> SignupScreen(
            onGoLogin = { currentScreen = Screen.Login }
        )

        Screen.ResetPassword -> ResetPasswordScreen(
            onContinue = { currentScreen = Screen.EmailVerification },
            onBackToLogin = { currentScreen = Screen.Login }
        )

        Screen.EmailVerification -> EmailVerificationScreen(
            onVerify = { currentScreen = Screen.CreateNewPassword },
            onResend = { }
        )

        Screen.CreateNewPassword -> CreateNewPasswordScreen(
            onResetDone = { currentScreen = Screen.Login }
        )

        Screen.Home -> HomeScreen(
            firstName = userFirstName,
            onCategoryClick = { categoryKey ->
                // later navigate by category
            },
            onTabSelected = { tab ->
                // later navigate bottom tabs
            }
        )
    }
}