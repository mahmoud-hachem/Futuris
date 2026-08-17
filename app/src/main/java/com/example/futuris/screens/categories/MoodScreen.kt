package com.example.futuris.screens.categories

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.data.AlertItem
import com.example.futuris.data.AlertMemoryStore
import com.example.futuris.data.ChatMemoryStore
import com.example.futuris.data.OnboardingStateManager
import com.example.futuris.data.OnlineInsightManager
import com.example.futuris.data.QuizMemoryStore
import com.example.futuris.model.QuizAnswer
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar
import kotlinx.coroutines.launch

@Suppress("UNUSED_PARAMETER")
@Composable
fun MoodScreen(
    currentTab: String,
    onBackClick: () -> Unit,
    onTabSelected: (String) -> Unit,
    onQuestionClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val prefs = remember {
        context.getSharedPreferences("FuturisPrefs", Context.MODE_PRIVATE)
    }

    val savedFirstName = remember {
        prefs.getString("firstName", "")?.trim().orEmpty()
    }

    val savedLastName = remember {
        prefs.getString("lastName", "")?.trim().orEmpty()
    }

    val savedUsername = remember {
        prefs.getString("username", "")?.trim().orEmpty()
    }

    val savedEmail = remember {
        prefs.getString("email", "")?.trim().orEmpty()
    }

    val savedGender = remember {
        prefs.getString("gender", "")?.trim().orEmpty()
    }

    val savedLifeFocus = remember {
        prefs.getString("lifeFocus", "")?.trim().orEmpty()
    }

    val savedState = remember {
        prefs.getString("state", "")?.trim().orEmpty()
    }

    val savedIntent = remember {
        prefs.getString("intent", "")?.trim().orEmpty()
    }

    val userId = remember {
        prefs.getString("userId", "")?.trim().orEmpty()
            .ifBlank { "default_user" }
    }

    val savedDateOfBirth = remember {
        prefs.getString("dateOfBirth", "12/12/2000")?.trim().orEmpty()
            .ifBlank { "12/12/2000" }
    }

    val onboardingFinished = remember(userId) {
        OnboardingStateManager.isOnboardingFinished(
            context = context,
            userId = userId
        )
    }

    val quizAnswers = remember(userId) {
        QuizMemoryStore.getAnswers(userId)
    }

    val baseChatMessages = remember {
        ChatMemoryStore.getMessages()
    }

    var finalText by remember {
        mutableStateOf("Reading your mood and energy flow...")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var selectedQuestion by remember {
        mutableStateOf("")
    }

    val generatedQuestions = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(
        userId,
        savedFirstName,
        savedDateOfBirth,
        quizAnswers,
        baseChatMessages,
        onboardingFinished
    ) {
        isLoading = true
        selectedQuestion = ""
        generatedQuestions.clear()

        if (!onboardingFinished) {
            finalText =
                "Your aura is still forming... but your first 5 signals are missing. " +
                        "Complete the Destiny Quiz and I’ll unlock your true mood and energy prediction."
            isLoading = false
            return@LaunchedEffect
        }

        finalText = "Reading your mood and energy flow..."

        try {
            val response = OnlineInsightManager.generateCategoryInsight(
                userId = userId,
                firstName = savedFirstName,
                lastName = savedLastName,
                username = savedUsername,
                email = savedEmail,
                gender = savedGender,
                dateOfBirth = savedDateOfBirth,
                category = "mood_energy",
                lifeFocus = savedLifeFocus,
                state = savedState,
                intent = savedIntent,
                quizAnswers = quizAnswers,
                chatMessages = baseChatMessages
            )

            if (response.success && response.insight.isNotBlank()) {
                finalText = response.insight
                generatedQuestions.addAll(cleanMoodQuestions(response.questions))

                AlertMemoryStore.addAlert(
                    context = context,
                    alert = AlertItem(
                        id = System.currentTimeMillis().toString(),
                        title = "Mood Signal Updated",
                        message = "A new mood insight has been generated.",
                        timeLabel = "Now",
                        category = "mood",
                        isNew = true
                    )
                )
            } else {
                finalText = "Live insight is unavailable right now. Please try again."
            }
        } catch (e: Exception) {
            finalText = "Live insight is unavailable right now. Please try again."
        }

        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        MoodBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(bottom = 92.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                MoodBackButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onBackClick
                )

                Text(
                    text = "Mood & Energy",
                    color = Color.White,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            MoodCenterImage()

            Spacer(modifier = Modifier.height(22.dp))

            MoodPredictionCard(
                text = finalText,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (!onboardingFinished) {
                LockedCategoryHint(
                    text = "Complete the Destiny Quiz to unlock your real mood and energy prediction.",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))
            }

            if (onboardingFinished && !isLoading && generatedQuestions.isNotEmpty()) {
                QuestionsSectionTitle()

                Spacer(modifier = Modifier.height(10.dp))

                generatedQuestions.forEach { question ->
                    QuestionChip(
                        text = question,
                        isSelected = selectedQuestion == question,
                        borderColor = Color(0xFF9DE8FF),
                        glowColor = Color(0x449DE8FF),
                        onClick = {
                            selectedQuestion = question
                            onQuestionClick(question)

                            scope.launch {
                                val previousInsight = finalText

                                try {
                                    isLoading = true
                                    finalText = "Wait for your new insight..."
                                    generatedQuestions.clear()

                                    val followUpChatMessages = buildList {
                                        addAll(baseChatMessages)
                                        add("Previous mood and energy insight: $previousInsight")
                                        add("Selected follow-up question: $question")
                                        add(
                                            "Generate a NEW and DIFFERENT mood and energy insight based on the selected question. " +
                                                    "Do not repeat the previous insight. " +
                                                    "Do not mention the user's name. " +
                                                    "Answer in 2 to 4 sentences only. " +
                                                    "Also generate exactly two new follow-up questions."
                                        )
                                    }

                                    val followUpQuizAnswers = buildFollowUpQuizAnswers(
                                        originalAnswers = quizAnswers,
                                        selectedQuestion = question,
                                        previousInsight = previousInsight,
                                        categoryKey = "mood_energy"
                                    )

                                    val followUpResponse =
                                        OnlineInsightManager.generateCategoryInsight(
                                            userId = userId,
                                            firstName = savedFirstName,
                                            lastName = savedLastName,
                                            username = savedUsername,
                                            email = savedEmail,
                                            gender = savedGender,
                                            dateOfBirth = savedDateOfBirth,
                                            category = "mood_energy",
                                            lifeFocus = savedLifeFocus,
                                            state = savedState,
                                            intent = mergeIntentWithQuestion(
                                                currentIntent = savedIntent,
                                                question = question,
                                                previousInsight = previousInsight,
                                                categoryLabel = "Mood & Energy"
                                            ),
                                            quizAnswers = followUpQuizAnswers,
                                            chatMessages = followUpChatMessages
                                        )

                                    if (followUpResponse.success && followUpResponse.insight.isNotBlank()) {
                                        finalText = followUpResponse.insight
                                        generatedQuestions.addAll(
                                            cleanMoodQuestions(followUpResponse.questions)
                                        )

                                        AlertMemoryStore.addAlert(
                                            context = context,
                                            alert = AlertItem(
                                                id = System.currentTimeMillis().toString(),
                                                title = "Mood Follow-Up Ready",
                                                message = "A deeper mood insight has been unlocked.",
                                                timeLabel = "Now",
                                                category = "mood",
                                                isNew = true
                                            )
                                        )
                                    } else {
                                        finalText = "Live insight is unavailable right now. Please try again."
                                    }
                                } catch (e: Exception) {
                                    finalText = "Live insight is unavailable right now. Please try again."
                                }

                                isLoading = false
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            GlassBottomBar(
                selectedTab = currentTab,
                tabs = listOf(
                    BottomTabItem("Home", "home", R.drawable.nav_home),
                    BottomTabItem("Chat", "chat", R.drawable.nav_chat),
                    BottomTabItem("Alerts", "alerts", R.drawable.nav_alerts),
                    BottomTabItem("Profile", "profile", R.drawable.nav_profile)
                ),
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
private fun MoodBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC071826),
                            Color(0xAA15304A),
                            Color(0xD409121C)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x359DE8FF),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
private fun MoodCenterImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(190.dp)
                .blur(42.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x559DE8FF),
                            Color(0x3340A7C8),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Image(
            painter = painterResource(id = R.drawable.card_mood),
            contentDescription = "Mood category image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(175.dp)
        )
    }
}

@Composable
private fun MoodPredictionCard(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .widthIn(max = 380.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x4D78DFFF),
                        Color(0x3D2B8DAA)
                    )
                )
            )
            .border(
                BorderStroke(1.3.dp, Color(0x88D7F7FF)),
                RoundedCornerShape(26.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFC3F2FF),
                                    Color(0xFF69B9E7),
                                    Color(0xFF16314C)
                                )
                            )
                        )
                        .border(
                            BorderStroke(1.dp, Color(0x99FFFFFF)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✦",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Mood & Energy Prediction",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(165.dp)
                            .verticalScroll(scrollState),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(28.dp),
                                    color = Color.White,
                                    strokeWidth = 2.6.dp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            Text(
                                text = text,
                                color = Color(0xFFF4FCFF),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LockedCategoryHint(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x33071722),
                        Color(0x55305067),
                        Color(0x33071722)
                    )
                )
            )
            .border(
                BorderStroke(1.dp, Color(0x779DE8FF)),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color(0x449DE8FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👁",
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = text,
                color = Color(0xFFF4FCFF),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MoodBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.size(46.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .blur(18.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x559DE8FF),
                            Color(0x223C9FC1),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF79D8FF),
                            Color(0xFF2B5F89)
                        )
                    )
                )
                .border(
                    BorderStroke(1.dp, Color(0x99FFFFFF)),
                    CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuestionsSectionTitle() {
    Text(
        text = "Popular Questions",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun QuestionChip(
    text: String,
    isSelected: Boolean,
    borderColor: Color,
    glowColor: Color,
    onClick: () -> Unit
) {
    val finalBorder = if (isSelected) borderColor else Color(0x9988CFFF)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x40112532),
                        Color(0x55244C72),
                        Color(0x40112532)
                    )
                )
            )
            .border(
                BorderStroke(1.1.dp, finalBorder),
                RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) glowColor else Color(0x229DE8FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✦",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "➜",
                color = Color(0xFFD4F7FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun buildFollowUpQuizAnswers(
    originalAnswers: List<QuizAnswer>,
    selectedQuestion: String,
    previousInsight: String,
    categoryKey: String
): List<QuizAnswer> {
    return originalAnswers +
            QuizAnswer(
                questionId = "${categoryKey}_follow_up_question",
                selectedOptionText = selectedQuestion
            ) +
            QuizAnswer(
                questionId = "${categoryKey}_previous_insight",
                selectedOptionText = previousInsight
            )
}

private fun mergeIntentWithQuestion(
    currentIntent: String,
    question: String,
    previousInsight: String,
    categoryLabel: String
): String {
    val base = currentIntent.trim()

    val followUpBlock =
        "$categoryLabel follow-up requested. " +
                "Selected question: $question. " +
                "Previous insight: $previousInsight. " +
                "Generate a new and different follow-up insight without repeating the previous wording. " +
                "Also generate exactly two fresh follow-up questions."

    return if (base.isBlank()) {
        followUpBlock
    } else {
        "$base | $followUpBlock"
    }
}

private fun cleanMoodQuestions(questions: List<String>): List<String> {
    return questions
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .take(2)
}