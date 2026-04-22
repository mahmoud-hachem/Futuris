package com.example.futuris.screens.categories

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.futuris.data.AlertItem
import com.example.futuris.data.AlertMemoryStore
import com.example.futuris.data.QuizMemoryStore
import com.example.futuris.model.QuizQuestion

@Composable
fun QuizScreen(
    userId: String,
    questions: List<QuizQuestion>,
    onQuizFinished: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val prefs = remember {
        context.getSharedPreferences("FuturisPrefs", Context.MODE_PRIVATE)
    }

    val savedAnswers = remember(userId, questions) {
        QuizMemoryStore.getAnswers(userId)
    }

    var currentQuestionIndex by remember(userId, questions) {
        mutableIntStateOf(
            savedAnswers.size.coerceAtMost(
                if (questions.isNotEmpty()) questions.lastIndex else 0
            )
        )
    }

    if (questions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF12061E))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No quiz questions available.",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        return
    }

    LaunchedEffect(userId, questions.size) {
        val alreadySavedAnswers = QuizMemoryStore.getAnswers(userId)

        if (alreadySavedAnswers.size >= questions.size) {
            val quizAlertKey = "quiz_completed_alert_$userId"

            if (!prefs.getBoolean(quizAlertKey, false)) {
                AlertMemoryStore.addAlert(
                    context = context,
                    alert = AlertItem(
                        id = System.currentTimeMillis().toString(),
                        title = "Destiny Quiz Completed",
                        message = "Your core signals are now unlocked.",
                        timeLabel = "Now",
                        category = "system",
                        isNew = true
                    )
                )

                prefs.edit().putBoolean(quizAlertKey, true).apply()
            }

            onQuizFinished()
        }
    }

    val safeIndex = currentQuestionIndex.coerceIn(0, questions.lastIndex)
    val currentQuestion = questions[safeIndex]
    val progress = (safeIndex + 1).toFloat() / questions.size.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF14071F),
                        Color(0xFF1C0B2C),
                        Color(0xFF2B0F46)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33FFFFFF))
                        .border(
                            width = 1.dp,
                            color = Color(0x44FFFFFF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "←",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Destiny Quiz",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable { onBackClick() }
                        .padding(horizontal = 12.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = "Quit",
                        color = Color(0xFFE7D8FF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Question ${safeIndex + 1} of ${questions.size}",
                color = Color(0xFFE9D9FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(Color(0x33FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFB983FF),
                                    Color(0xFF8A5BFF)
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x44FFFFFF),
                                Color(0x22FFFFFF)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0x33FFFFFF),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "Reveal your next signal",
                        color = Color(0xFFE2CFFF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentQuestion.text,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 30.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(currentQuestion.options) { option ->
                    Button(
                        onClick = {
                            QuizMemoryStore.saveAnswer(
                                userId = userId,
                                questionId = currentQuestion.id,
                                selectedOptionText = option.text
                            )

                            val updatedAnswers = QuizMemoryStore.getAnswers(userId)

                            if (updatedAnswers.size >= questions.size) {
                                val quizAlertKey = "quiz_completed_alert_$userId"

                                if (!prefs.getBoolean(quizAlertKey, false)) {
                                    AlertMemoryStore.addAlert(
                                        context = context,
                                        alert = AlertItem(
                                            id = System.currentTimeMillis().toString(),
                                            title = "Destiny Quiz Completed",
                                            message = "Your core signals are now unlocked.",
                                            timeLabel = "Now",
                                            category = "system",
                                            isNew = true
                                        )
                                    )

                                    prefs.edit().putBoolean(quizAlertKey, true).apply()
                                }

                                onQuizFinished()
                            } else {
                                currentQuestionIndex = updatedAnswers.size
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7352B6),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = option.text,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "You can leave this quiz anytime and return later.",
                        color = Color(0xFFD8C6EF),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}