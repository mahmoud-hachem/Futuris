package com.example.futuris.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.futuris.screens.alerts.AlertsScreen
import com.example.futuris.screens.auth.CreateNewPasswordScreen
import com.example.futuris.screens.auth.EmailVerificationScreen
import com.example.futuris.screens.auth.LoginScreen
import com.example.futuris.screens.auth.ResetPasswordScreen
import com.example.futuris.screens.auth.SignupScreen
import com.example.futuris.screens.auth.SplashScreen
import com.example.futuris.screens.categories.LoveScreen
import com.example.futuris.screens.chat.ChatScreen
import com.example.futuris.screens.home.HomeScreen
import com.example.futuris.screens.profile.ProfileScreen

enum class Screen {
    Splash,
    Login,
    Signup,
    ResetPassword,
    EmailVerification,
    CreateNewPassword,
    Home,
    Love,
    Chat,
    Alerts,
    Profile
}

@Composable
fun FuturisApp() {
    var currentScreen by remember { mutableStateOf(Screen.Signup) }

    // temporary frontend values
    var userFirstName by remember { mutableStateOf("Alex") }
    var userLastName by remember { mutableStateOf("Johnson") }
    var userEmail by remember { mutableStateOf("alex@email.com") }

    when (currentScreen) {
        Screen.Splash -> SplashScreen(
            onDone = { currentScreen = Screen.Home }
        )

        Screen.Login -> LoginScreen(
            onGoSignup = { currentScreen = Screen.Signup },
            onForgotPassword = { currentScreen = Screen.ResetPassword },
            onLoginSuccess = { currentScreen = Screen.Home }
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
            currentTab = "home",
            onCategoryClick = { category ->
                when (category) {
                    "love" -> currentScreen = Screen.Love
                }
            },
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )

        Screen.Love -> LoveScreen(
            currentTab = "home",
            onBackClick = { currentScreen = Screen.Home },
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            },
            onQuestionClick = {
                currentScreen = Screen.Chat
            }
        )

        Screen.Chat -> ChatScreen(
            firstName = userFirstName,
            currentTab = "chat",
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )

        Screen.Alerts -> AlertsScreen(
            currentTab = "alerts",
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )

        Screen.Profile -> ProfileScreen(
            firstName = userFirstName,
            lastName = userLastName,
            email = userEmail,
            currentTab = "profile",
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )
    }
}