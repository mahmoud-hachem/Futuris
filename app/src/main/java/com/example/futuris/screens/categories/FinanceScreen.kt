package com.example.futuris.screens.categories

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.data.ChatMemoryStore
import com.example.futuris.data.QuizMemoryStore
import com.example.futuris.prediction.PredictionBrain
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar

@Composable
fun FinanceScreen(
    currentTab: String,
    onBackClick: () -> Unit,
    onTabSelected: (String) -> Unit,
    onQuestionClick: (String) -> Unit = {}
) {
    val context = LocalContext.current

    val prefs = remember {
        context.getSharedPreferences("FuturisPrefs", Context.MODE_PRIVATE)
    }

    val savedFirstName = remember {
        prefs.getString("firstName", "")?.trim().orEmpty()
    }

    val savedDateOfBirth = remember {
        prefs.getString("dateOfBirth", "12/12/2000")?.trim().orEmpty()
            .ifBlank { "12/12/2000" }
    }

    val userId = remember {
        prefs.getString("userId", "")?.trim().orEmpty()
            .ifBlank { "default_user" }
    }

    val quizAnswers = remember(userId) {
        QuizMemoryStore.getAnswers(userId)
    }

    val chatMessages = remember {
        ChatMemoryStore.getMessages()
    }

    val finalText = remember(savedFirstName, savedDateOfBirth, quizAnswers, chatMessages) {
        PredictionBrain.generateInsight(
            category = "finance",
            firstName = savedFirstName,
            dateOfBirth = savedDateOfBirth,
            quizAnswers = quizAnswers,
            chatMessages = chatMessages
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        FinanceMagicalFillLayer(
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 92.dp)
        ) {
            Text(
                text = "Finance",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 14.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .border(
                        BorderStroke(1.dp, Color(0x55FFFFFF)),
                        CircleShape
                    )
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Image(
                painter = painterResource(id = R.drawable.futuris_genie),
                contentDescription = "Futuris Genie",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(520.dp)
                    .offset(x = (-72).dp, y = 150.dp)
            )

            FinanceSpeechBubble(
                text = finalText,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(300.dp)
                    .padding(top = 55.dp, end = 50.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 10.dp)
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
private fun FinanceMagicalFillLayer(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x60FFD76A),
                    Color(0x28B8871A),
                    Color.Transparent
                ),
                center = center.copy(x = size.width * 0.62f, y = size.height * 0.36f),
                radius = size.minDimension * 0.34f
            ),
            radius = size.minDimension * 0.34f,
            center = center.copy(x = size.width * 0.62f, y = size.height * 0.36f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x34FFE7A8),
                    Color(0x16C5962B),
                    Color.Transparent
                ),
                center = center.copy(x = size.width * 0.72f, y = size.height * 0.58f),
                radius = size.minDimension * 0.28f
            ),
            radius = size.minDimension * 0.28f,
            center = center.copy(x = size.width * 0.72f, y = size.height * 0.58f)
        )
    }
}

@Composable
private fun FinanceSpeechBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xF9FFFFFF),
                            Color(0xFFF8F0D9)
                        )
                    )
                )
                .border(
                    BorderStroke(1.dp, Color(0xCCFFFFFF)),
                    RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Text(
                text = text,
                color = Color(0xFF6D4C09),
                fontSize = 14.sp,
                lineHeight = 23.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Canvas(
            modifier = Modifier
                .padding(start = 42.dp)
                .offset(y = (-1).dp)
                .size(width = 46.dp, height = 32.dp)
        ) {
            val path = Path().apply {
                moveTo(size.width * 0.10f, 0f)
                lineTo(size.width * 0.90f, 0f)
                lineTo(size.width * 0.30f, size.height)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xF9FFFFFF),
                        Color(0xFFF8F0D9)
                    )
                ),
                style = Fill
            )
        }
    }
}