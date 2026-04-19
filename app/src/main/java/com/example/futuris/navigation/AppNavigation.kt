package com.example.futuris.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.futuris.data.QuizMemoryStore
import com.example.futuris.data.QuizRepository
import com.example.futuris.screens.alerts.AlertsScreen
import com.example.futuris.screens.auth.CreateNewPasswordScreen
import com.example.futuris.screens.auth.EmailVerificationScreen
import com.example.futuris.screens.auth.LoginScreen
import com.example.futuris.screens.auth.ResetPasswordScreen
import com.example.futuris.screens.auth.SignupScreen
import com.example.futuris.screens.auth.SplashScreen
import com.example.futuris.screens.categories.CareerScreen
import com.example.futuris.screens.categories.DecisionsScreen
import com.example.futuris.screens.categories.FinanceScreen
import com.example.futuris.screens.categories.LifePathScreen
import com.example.futuris.screens.categories.LoveScreen
import com.example.futuris.screens.categories.MoodScreen
import com.example.futuris.screens.categories.QuizScreen
import com.example.futuris.screens.chat.ChatScreen
import com.example.futuris.screens.home.HomeScreen
import com.example.futuris.screens.profile.AccountInfoUiState
import com.example.futuris.screens.profile.AccountInformationScreen
import com.example.futuris.screens.profile.NotificationsScreen
import com.example.futuris.screens.profile.PrivacySecurityScreen
import com.example.futuris.screens.profile.HelpSupportScreen
import com.example.futuris.screens.profile.ProfileScreen

enum class Screen {
    Splash,
    Login,
    Signup,
    ResetPassword,
    EmailVerification,
    CreateNewPassword,
    Home,
    Quiz,
    Love,
    Career,
    Finance,
    Mood,
    Decisions,
    LifePath,
    Chat,
    Alerts,
    Profile,
    AccountInformation,
    Notifications,
    PrivacySecurity,
    HelpSupport
}

@Composable
fun FuturisApp() {
    val context = LocalContext.current

    QuizMemoryStore.init(context)

    val prefs = context.getSharedPreferences(
        "FuturisPrefs",
        Context.MODE_PRIVATE
    )

    val savedLogin = prefs.getBoolean("isLoggedIn", false)
    val savedUsername = prefs.getString("username", "Alex") ?: "Alex"
    val savedFirstName = prefs.getString("firstName", "") ?: ""
    val savedLastName = prefs.getString("lastName", "") ?: ""
    val savedEmail = prefs.getString("email", "alex@email.com") ?: "alex@email.com"
    val savedDateOfBirth = prefs.getString("dateOfBirth", "") ?: ""
    val savedGender = prefs.getString("gender", "") ?: ""
    val savedUserId = prefs.getString("userId", savedEmail)?.ifBlank { savedEmail } ?: savedEmail
    val savedLifeFocus = prefs.getString("lifeFocus", "") ?: ""
    val savedNotificationsEnabled = prefs.getBoolean("notificationsEnabled", true)
    val savedInsightReminders = prefs.getBoolean("insightReminders", true)
    val savedBaseQuizCompleted = prefs.getBoolean("isBaseQuizCompleted", false)

    var currentScreen by remember {
        mutableStateOf(
            when {
                !savedLogin -> Screen.Login
                !savedBaseQuizCompleted -> Screen.Quiz
                else -> Screen.Home
            }
        )
    }

    var userUsername by remember { mutableStateOf(savedUsername) }
    var userFirstName by remember { mutableStateOf(savedFirstName) }
    var userLastName by remember { mutableStateOf(savedLastName) }
    var userEmail by remember { mutableStateOf(savedEmail) }
    var userDateOfBirth by remember { mutableStateOf(savedDateOfBirth) }
    var userGender by remember { mutableStateOf(savedGender) }
    var userLifeFocus by remember { mutableStateOf(savedLifeFocus) }
    var userId by remember { mutableStateOf(savedUserId) }
    var passwordChangeReturnScreen by remember { mutableStateOf(Screen.Login) }
    var notificationsEnabled by remember { mutableStateOf(savedNotificationsEnabled) }
    var insightReminders by remember { mutableStateOf(savedInsightReminders) }

    when (currentScreen) {
        Screen.Splash -> SplashScreen(
            onDone = {
                currentScreen = when {
                    !prefs.getBoolean("isLoggedIn", false) -> Screen.Login
                    !prefs.getBoolean("isBaseQuizCompleted", false) -> Screen.Quiz
                    else -> Screen.Home
                }
            }
        )

        Screen.Login -> LoginScreen(
            onGoSignup = { currentScreen = Screen.Signup },
            onForgotPassword = { currentScreen = Screen.ResetPassword },
            onLoginSuccess = { username, firstName, lastName, email ->
                userUsername = if (username.isNotBlank()) username else "User"
                userFirstName = firstName
                userLastName = lastName
                userEmail = email
                userId = email.ifBlank { "default_user" }

                prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("username", userUsername)
                    .putString("firstName", userFirstName)
                    .putString("lastName", userLastName)
                    .putString("email", userEmail)
                    .putString("userId", userId)
                    .apply()

                val isBaseQuizCompleted =
                    prefs.getBoolean("isBaseQuizCompleted", false)

                currentScreen =
                    if (isBaseQuizCompleted) Screen.Home else Screen.Quiz
            }
        )

        Screen.Signup -> SignupScreen(
            onGoLogin = { currentScreen = Screen.Login }
        )

        Screen.ResetPassword -> ResetPasswordScreen(
            onContinue = { currentScreen = Screen.EmailVerification },
            onBackToLogin = { currentScreen = passwordChangeReturnScreen }
        )

        Screen.EmailVerification -> EmailVerificationScreen(
            onVerify = { currentScreen = Screen.CreateNewPassword },
            onResend = { }
        )

        Screen.CreateNewPassword -> CreateNewPasswordScreen(
            onResetDone = { currentScreen = passwordChangeReturnScreen }
        )

        Screen.Home -> HomeScreen(
            firstName = if (userFirstName.isNotBlank()) userFirstName else userUsername,
            currentTab = "home",
            onCategoryClick = { category ->
                when (category) {
                    "love" -> currentScreen = Screen.Love
                    "career" -> currentScreen = Screen.Career
                    "finance" -> currentScreen = Screen.Finance
                    "mood" -> currentScreen = Screen.Mood
                    "decisions" -> currentScreen = Screen.Decisions
                    "lifepath" -> currentScreen = Screen.LifePath
                }
            },
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            },
            onDestinyQuizClick = {
                currentScreen = Screen.Quiz
            }
        )

        Screen.Quiz -> QuizScreen(
            userId = userId,
            questions = QuizRepository.mandatoryQuestions,
            onBackClick = {
                currentScreen = Screen.Home
            },
            onQuizFinished = {
                prefs.edit()
                    .putBoolean("isBaseQuizCompleted", true)
                    .apply()

                currentScreen = Screen.Home
            }
        )

        Screen.Career -> CareerScreen(
            currentTab = "home",
            onBackClick = { currentScreen = Screen.Home },
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )

        Screen.Finance -> FinanceScreen(
            currentTab = "home",
            onBackClick = { currentScreen = Screen.Home },
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )

        Screen.Mood -> MoodScreen(
            currentTab = "home",
            onBackClick = { currentScreen = Screen.Home },
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )

        Screen.Decisions -> DecisionsScreen(
            currentTab = "home",
            onBackClick = { currentScreen = Screen.Home },
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            }
        )

        Screen.LifePath -> LifePathScreen(
            currentTab = "home",
            onBackClick = { currentScreen = Screen.Home },
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
            firstName = if (userFirstName.isNotBlank()) userFirstName else userUsername,
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
            firstName = if (userFirstName.isNotBlank()) userFirstName else userUsername,
            lastName = userLastName,
            email = userEmail,
            dateOfBirth = userDateOfBirth,
            gender = userGender,
            username = userUsername,
            lifeFocus = userLifeFocus,
            currentTab = "profile",
            onTabSelected = { tab ->
                when (tab) {
                    "home" -> currentScreen = Screen.Home
                    "chat" -> currentScreen = Screen.Chat
                    "alerts" -> currentScreen = Screen.Alerts
                    "profile" -> currentScreen = Screen.Profile
                }
            },
            onOpenAccountInformation = {
                currentScreen = Screen.AccountInformation
            },
            onOpenNotifications = {
                currentScreen = Screen.Notifications
            },
            onOpenPrivacy = {
                currentScreen = Screen.PrivacySecurity
            },
            onOpenHelp = {
                currentScreen = Screen.HelpSupport
            },
            onLogout = {
                prefs.edit().clear().apply()

                QuizMemoryStore.clearAnswers(userId)

                userUsername = "Alex"
                userFirstName = ""
                userLastName = ""
                userEmail = "alex@email.com"
                userDateOfBirth = ""
                userGender = ""
                userLifeFocus = ""
                userId = "default_user"
                notificationsEnabled = true
                insightReminders = true
                currentScreen = Screen.Login
            }
        )

        Screen.AccountInformation -> AccountInformationScreen(
            userData = AccountInfoUiState(
                firstName = userFirstName,
                lastName = userLastName,
                username = userUsername,
                email = userEmail,
                dateOfBirth = userDateOfBirth,
                gender = userGender,
                notificationsEnabled = notificationsEnabled,
                insightReminders = insightReminders
            ),
            onBackClick = {
                currentScreen = Screen.Profile
            },
            onSaveClick = { updatedData ->
                userFirstName = updatedData.firstName
                userLastName = updatedData.lastName
                userUsername = updatedData.username
                userEmail = updatedData.email
                notificationsEnabled = updatedData.notificationsEnabled
                insightReminders = updatedData.insightReminders

                prefs.edit()
                    .putString("firstName", userFirstName)
                    .putString("lastName", userLastName)
                    .putString("username", userUsername)
                    .putString("email", userEmail)
                    .putString("userId", userId)
                    .putBoolean("notificationsEnabled", notificationsEnabled)
                    .putBoolean("insightReminders", insightReminders)
                    .apply()
            },
            onChangePhotoClick = {
            },
            onChangePasswordClick = {
                passwordChangeReturnScreen = Screen.Profile
                currentScreen = Screen.ResetPassword
            }
        )

        Screen.Notifications -> NotificationsScreen(
            notificationsEnabled = notificationsEnabled,
            insightReminders = insightReminders,
            onBackClick = { currentScreen = Screen.Profile },
            onSave = { notifEnabled, insightRemind ->
                notificationsEnabled = notifEnabled
                insightReminders = insightRemind
                prefs.edit()
                    .putBoolean("notificationsEnabled", notificationsEnabled)
                    .putBoolean("insightReminders", insightReminders)
                    .apply()
                currentScreen = Screen.Profile
            }
        )

        Screen.PrivacySecurity -> PrivacySecurityScreen(
            onBackClick = { currentScreen = Screen.Profile },
            onChangePassword = {
                passwordChangeReturnScreen = Screen.Profile
                currentScreen = Screen.ResetPassword
            }
        )

        Screen.HelpSupport -> HelpSupportScreen(
            onBackClick = { currentScreen = Screen.Profile }
        )
    }
}