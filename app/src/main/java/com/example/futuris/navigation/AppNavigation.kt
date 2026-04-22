package com.example.futuris.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.futuris.data.OnboardingStateManager
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
import com.example.futuris.screens.profile.HelpSupportScreen
import com.example.futuris.screens.profile.NotificationPreferencesScreen
import com.example.futuris.screens.profile.PrivacySecurityScreen
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
    NotificationPreferences,
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
    val savedProfileImageUri = prefs.getString("profileImageUri", "") ?: ""
    val savedUserId = prefs.getString("userId", savedEmail)?.ifBlank { savedEmail } ?: savedEmail
    val savedNotificationsEnabled = prefs.getBoolean("notificationsEnabled", true)
    val savedInsightReminders = prefs.getBoolean("insightReminders", true)

    val savedOnboardingFinished = OnboardingStateManager.isOnboardingFinished(
        context = context,
        userId = savedUserId
    )

    var currentScreen by remember {
        mutableStateOf(
            when {
                !savedLogin -> Screen.Login
                !savedOnboardingFinished -> Screen.Quiz
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
    var profileImageUri by remember { mutableStateOf(savedProfileImageUri) }
    var userId by remember { mutableStateOf(savedUserId) }
    var notificationsEnabled by remember { mutableStateOf(savedNotificationsEnabled) }
    var insightReminders by remember { mutableStateOf(savedInsightReminders) }

    when (currentScreen) {
        Screen.Splash -> SplashScreen(
            onDone = {
                val currentSavedLogin = prefs.getBoolean("isLoggedIn", false)
                val currentSavedEmail = prefs.getString("email", "alex@email.com") ?: "alex@email.com"
                val currentSavedUserId =
                    prefs.getString("userId", currentSavedEmail)?.ifBlank { currentSavedEmail }
                        ?: currentSavedEmail

                val onboardingFinished = OnboardingStateManager.isOnboardingFinished(
                    context = context,
                    userId = currentSavedUserId
                )

                currentScreen = when {
                    !currentSavedLogin -> Screen.Login
                    !onboardingFinished -> Screen.Quiz
                    else -> Screen.Home
                }
            }
        )

        Screen.Login -> LoginScreen(
            onGoSignup = { currentScreen = Screen.Signup },
            onForgotPassword = { currentScreen = Screen.ResetPassword },
            onGoEmailVerification = { currentScreen = Screen.EmailVerification },
            onLoginSuccess = { username, firstName, lastName, email, dateOfBirth, gender ->
                userUsername = if (username.isNotBlank()) username else "User"
                userFirstName = firstName
                userLastName = lastName
                userEmail = email
                userDateOfBirth = dateOfBirth
                userGender = gender
                userId = email.ifBlank { "default_user" }

                prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("username", userUsername)
                    .putString("firstName", userFirstName)
                    .putString("lastName", userLastName)
                    .putString("email", userEmail)
                    .putString("dateOfBirth", userDateOfBirth)
                    .putString("gender", userGender)
                    .putString("userId", userId)
                    .apply()

                val onboardingFinished = OnboardingStateManager.isOnboardingFinished(
                    context = context,
                    userId = userId
                )

                currentScreen = if (onboardingFinished) Screen.Home else Screen.Quiz
            }
        )

        Screen.Signup -> SignupScreen(
            onGoLogin = { currentScreen = Screen.Login },
            onGoEmailVerification = { currentScreen = Screen.EmailVerification }
        )

        Screen.ResetPassword -> ResetPasswordScreen(
            onContinue = { currentScreen = Screen.Login },
            onBackToLogin = { currentScreen = Screen.Login }
        )

        Screen.EmailVerification -> EmailVerificationScreen(
            onVerified = { currentScreen = Screen.Login },
            onGoLogin = { currentScreen = Screen.Login }
        )

        Screen.CreateNewPassword -> CreateNewPasswordScreen(
            currentEmail = userEmail,
            onResetDone = { currentScreen = Screen.AccountInformation },
            onBackClick = { currentScreen = Screen.PrivacySecurity }
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
                OnboardingStateManager.setQuizCompleted(
                    context = context,
                    userId = userId,
                    completed = true
                )

                OnboardingStateManager.setFirstInsightGenerated(
                    context = context,
                    userId = userId,
                    generated = true
                )

                OnboardingStateManager.setOnboardingFinished(
                    context = context,
                    userId = userId,
                    finished = true
                )

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
            profileImageUri = profileImageUri,
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
            onOpenNotificationPreferences = {
                currentScreen = Screen.NotificationPreferences
            },
            onOpenPrivacySecurity = {
                currentScreen = Screen.PrivacySecurity
            },
            onOpenHelpSupport = {
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
                profileImageUri = ""
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
                insightReminders = insightReminders,
                profileImageUri = profileImageUri
            ),
            onBackClick = {
                currentScreen = Screen.Profile
            },
            onSaveClick = { updatedData ->
                userFirstName = updatedData.firstName
                userLastName = updatedData.lastName
                userUsername = updatedData.username
                notificationsEnabled = updatedData.notificationsEnabled
                insightReminders = updatedData.insightReminders
                profileImageUri = updatedData.profileImageUri

                prefs.edit()
                    .putString("firstName", userFirstName)
                    .putString("lastName", userLastName)
                    .putString("username", userUsername)
                    .putString("email", userEmail)
                    .putString("userId", userId)
                    .putBoolean("notificationsEnabled", notificationsEnabled)
                    .putBoolean("insightReminders", insightReminders)
                    .putString("profileImageUri", profileImageUri)
                    .apply()

                currentScreen = Screen.Profile
            },
            onChangePasswordClick = {
                currentScreen = Screen.CreateNewPassword
            }
        )

        Screen.NotificationPreferences -> NotificationPreferencesScreen(
            notificationsEnabled = notificationsEnabled,
            insightReminders = insightReminders,
            onBackClick = { currentScreen = Screen.Profile },
            onSaveClick = { newNotificationsEnabled, newInsightReminders ->
                notificationsEnabled = newNotificationsEnabled
                insightReminders = newInsightReminders

                prefs.edit()
                    .putBoolean("notificationsEnabled", notificationsEnabled)
                    .putBoolean("insightReminders", insightReminders)
                    .apply()

                currentScreen = Screen.Profile
            }
        )

        Screen.PrivacySecurity -> PrivacySecurityScreen(
            email = userEmail,
            onBackClick = { currentScreen = Screen.Profile },
            onChangePasswordClick = { currentScreen = Screen.CreateNewPassword }
        )

        Screen.HelpSupport -> HelpSupportScreen(
            onBackClick = { currentScreen = Screen.Profile }
        )
    }
}