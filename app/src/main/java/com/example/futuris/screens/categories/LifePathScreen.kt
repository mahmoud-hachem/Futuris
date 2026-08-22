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
fun LifePathScreen(
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
        mutableStateOf("Reading the deeper direction of your life path...")
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
                "Your path is still behind the veil... the first 5 signals must appear before I can read it. " +
                        "Complete the Destiny Quiz and I’ll unlock your true life path prediction."
            isLoading = false
            return@LaunchedEffect
        }

        finalText = "Reading the deeper direction of your life path..."

        try {
            val response = OnlineInsightManager.generateCategoryInsight(
                userId = userId,
                firstName = savedFirstName,
                lastName = savedLastName,
                username = savedUsername,
                email = savedEmail,
                gender = savedGender,
                dateOfBirth = savedDateOfBirth,
                category = "life_path",
                lifeFocus = savedLifeFocus,
                state = savedState,
                intent = savedIntent,
                quizAnswers = quizAnswers,
                chatMessages = baseChatMessages
            )

            if (response.success && response.insight.isNotBlank()) {
                finalText = response.insight
                generatedQuestions.addAll(cleanLifePathQuestions(response.questions))

                AlertMemoryStore.addAlert(
                    context = context,
                    alert = AlertItem(
                        id = System.currentTimeMillis().toString(),
                        title = "Life Path Signal Updated",
                        message = "A new life path insight has been generated.",
                        timeLabel = "Now",
                        category = "lifepath",
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
        LifePathBackground()

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
                LifePathBackButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onBackClick
                )

                Text(
                    text = "Life Path",
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

            LifePathCenterImage()

            Spacer(modifier = Modifier.height(22.dp))

            LifePathPredictionCard(
                text = finalText,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (!onboardingFinished) {
                LockedLifePathHint(
                    text = "Complete the Destiny Quiz to unlock your real life path prediction.",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))
            }

            if (onboardingFinished && !isLoading && generatedQuestions.isNotEmpty()) {
                LifePathQuestionsTitle()

                Spacer(modifier = Modifier.height(10.dp))

                generatedQuestions.forEach { question ->
                    LifePathQuestionChip(
                        text = question,
                        isSelected = selectedQuestion == question,
                        borderColor = Color(0xFF5E60CE),
                        glowColor = Color(0x44C6B8FF),
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
                                        add("Previous life path insight: $previousInsight")
                                        add("Selected follow-up question: $question")
                                        add(
                                            "Generate a NEW and DIFFERENT life path insight based on the selected question. " +
                                                    "Do not repeat the previous insight. " +
                                                    "Do not mention the user's name. " +
                                                    "Answer in 2 to 4 sentences only. " +
                                                    "Also generate exactly two new follow-up questions."
                                        )
                                    }

                                    val followUpQuizAnswers = buildLifePathFollowUpQuizAnswers(
                                        originalAnswers = quizAnswers,
                                        selectedQuestion = question,
                                        previousInsight = previousInsight,
                                        categoryKey = "life_path"
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
                                            category = "life_path",
                                            lifeFocus = savedLifeFocus,
                                            state = savedState,
                                            intent = mergeLifePathIntentWithQuestion(
                                                currentIntent = savedIntent,
                                                question = question,
                                                previousInsight = previousInsight,
                                                categoryLabel = "Life Path"
                                            ),
                                            quizAnswers = followUpQuizAnswers,
                                            chatMessages = followUpChatMessages
                                        )

                                    if (followUpResponse.success && followUpResponse.insight.isNotBlank()) {
                                        finalText = followUpResponse.insight
                                        generatedQuestions.addAll(
                                            cleanLifePathQuestions(followUpResponse.questions)
                                        )

                                        AlertMemoryStore.addAlert(
                                            context = context,
                                            alert = AlertItem(
                                                id = System.currentTimeMillis().toString(),
                                                title = "Life Path Follow-Up Ready",
                                                message = "A deeper life path insight has been unlocked.",
                                                timeLabel = "Now",
                                                category = "lifepath",
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
private fun LifePathBackground() {
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
                            Color(0xCC15163C),
                            Color(0xAA3F37C9),
                            Color(0xD40E1028)
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
                            Color(0x355E60CE),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
private fun LifePathCenterImage() {
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
                            Color(0x555E60CE),
                            Color(0x333F37C9),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Image(
            painter = painterResource(id = R.drawable.lifepath),
            contentDescription = "Life path category image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(230.dp)
        )
    }
}

@Composable
private fun LifePathPredictionCard(
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
                        Color(0x4D5E60CE),
                        Color(0x3D3F37C9)
                    )
                )
            )
            .border(
                BorderStroke(1.3.dp, Color(0x88C6B8FF)),
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
                                    Color(0xFFC6B8FF),
                                    Color(0xFF5E60CE),
                                    Color(0xFF3F37C9)
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
                        text = "Your Life Path Prediction",
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
                                color = Color(0xFFF7F6FF),
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
private fun LockedLifePathHint(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x3314162B),
                        Color(0x554745A5),
                        Color(0x3314162B)
                    )
                )
            )
            .border(
                BorderStroke(1.dp, Color(0x775E60CE)),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color(0x445E60CE)),
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
                color = Color(0xFFF7F6FF),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun LifePathBackButton(
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
                            Color(0x555E60CE),
                            Color(0x223F37C9),
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
                            Color(0xFF8E90F3),
                            Color(0xFF3F37C9)
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
private fun LifePathQuestionsTitle() {
    Text(
        text = "Popular Questions",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun LifePathQuestionChip(
    text: String,
    isSelected: Boolean,
    borderColor: Color,
    glowColor: Color,
    onClick: () -> Unit
) {
    val finalBorder = if (isSelected) borderColor else Color(0x999B98FF)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x40151833),
                        Color(0x554341A8),
                        Color(0x40151833)
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
                    .background(if (isSelected) glowColor else Color(0x225E60CE)),
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
                color = Color(0xFFE0DCFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun buildLifePathFollowUpQuizAnswers(
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

private fun mergeLifePathIntentWithQuestion(
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

private fun cleanLifePathQuestions(questions: List<String>): List<String> {
    return questions
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .take(2)
}