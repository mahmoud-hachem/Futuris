package com.example.futuris.screens.categories
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.data.ChatMemoryStore
import com.example.futuris.data.OnlineInsightManager
import com.example.futuris.data.QuizMemoryStore
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar

@Suppress("UNUSED_PARAMETER")
@Composable
fun CareerScreen(
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

    val quizAnswers = remember(userId) {
        QuizMemoryStore.getAnswers(userId)
    }

    val chatMessages = remember {
        ChatMemoryStore.getMessages()
    }

    var finalText by remember {
        mutableStateOf("Reading your career signals...")
    }

    LaunchedEffect(userId, savedFirstName, savedDateOfBirth, quizAnswers, chatMessages) {
        val response = OnlineInsightManager.generateCategoryInsight(
            userId = userId,
            firstName = savedFirstName,
            lastName = savedLastName,
            username = savedUsername,
            email = savedEmail,
            gender = savedGender,
            dateOfBirth = savedDateOfBirth,
            category = "career",
            lifeFocus = savedLifeFocus,
            state = savedState,
            intent = savedIntent,
            quizAnswers = quizAnswers,
            chatMessages = chatMessages
        )

        finalText = if (response.insight.isNotBlank()) {
            response.insight
        } else {
            "Your career energy is forming quietly. Keep moving with patience and consistency."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        CareerScreenBackground(
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 94.dp)
        ) {
            Text(
                text = "Career & Studies",
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
                    .background(Color(0x26FFFFFF))
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

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 68.dp, start = 18.dp, end = 18.dp, bottom = 8.dp)
            ) {
                val genieHeight = if (maxHeight < 700.dp) 118.dp else 138.dp

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CareerGenieHero(
                        genieHeight = genieHeight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Current momentum reading",
                        color = Color(0xDDEEE5FF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.3.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    StarDivider()

                    Spacer(modifier = Modifier.height(16.dp))

                    CareerInsightCard(
                        text = finalText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
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
private fun CareerScreenBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
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
                            Color(0xA8140827),
                            Color(0x6E220B40),
                            Color(0xBF11061D)
                        )
                    )
                )
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val sparkles = listOf(
                Offset(size.width * 0.10f, size.height * 0.12f),
                Offset(size.width * 0.18f, size.height * 0.20f),
                Offset(size.width * 0.27f, size.height * 0.31f),
                Offset(size.width * 0.82f, size.height * 0.17f),
                Offset(size.width * 0.89f, size.height * 0.27f),
                Offset(size.width * 0.14f, size.height * 0.46f),
                Offset(size.width * 0.90f, size.height * 0.52f),
                Offset(size.width * 0.22f, size.height * 0.66f),
                Offset(size.width * 0.72f, size.height * 0.73f),
                Offset(size.width * 0.87f, size.height * 0.80f)
            )

            sparkles.forEachIndexed { index, point ->
                drawCircle(
                    color = if (index % 2 == 0) Color(0xCCFFFFFF) else Color(0x99E8D2FF),
                    radius = if (index % 3 == 0) 3.8f else 2.2f,
                    center = point
                )
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x354DABFF),
                        Color.Transparent
                    )
                ),
                center = Offset(size.width * 0.18f, size.height * 0.72f),
                radius = size.minDimension * 0.25f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x2CDB8CFF),
                        Color.Transparent
                    )
                ),
                center = Offset(size.width * 0.82f, size.height * 0.60f),
                radius = size.minDimension * 0.18f
            )
        }
    }
}

@Composable
private fun CareerGenieHero(
    genieHeight: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(genieHeight + 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(genieHeight + 28.dp)
                .blur(28.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x665E29C9),
                            Color(0x335841D8),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Image(
            painter = painterResource(id = R.drawable.futuris_genie),
            contentDescription = "Futuris Genie",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(genieHeight)
        )
    }
}

@Composable
private fun StarDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
        ) {
            val centerX = size.width / 2f
            val lineY = size.height * 0.55f

            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0x55FFFFFF),
                        Color(0x88E4D1FF),
                        Color(0x55FFFFFF),
                        Color.Transparent
                    )
                ),
                start = Offset(0f, lineY),
                end = Offset(size.width, lineY),
                strokeWidth = 2.2f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFD7BCFF),
                        Color.Transparent
                    )
                ),
                radius = 8f,
                center = Offset(centerX, lineY)
            )

            drawLine(
                color = Color(0x66FFFFFF),
                start = Offset(centerX, lineY - 8f),
                end = Offset(centerX, lineY + 8f),
                strokeWidth = 1.8f
            )
        }
    }
}

@Composable
private fun CareerInsightCard(
    text: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var cardSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .widthIn(max = 380.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xEEF4F0FF),
                        Color(0xEAEFFCFF),
                        Color(0xE7EEFFFF)
                    )
                )
            )
            .border(
                BorderStroke(1.dp, Color(0x90FFFFFF)),
                RoundedCornerShape(30.dp)
            )
            .onSizeChanged { cardSize = it }
    ) {
        if (cardSize != IntSize.Zero) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x30A28BFF),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.85f, size.height * 0.10f),
                        radius = size.minDimension * 0.60f
                    ),
                    cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx()),
                    style = Fill
                )

                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x22FFFFFF),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.15f, size.height * 0.07f),
                        radius = size.minDimension * 0.45f
                    ),
                    cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx()),
                    style = Fill
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Oracle Insight",
                color = Color(0xFF4B2A7A),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Live reading from your current signals",
                color = Color(0xCC6F5A98),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0x20FFFFFF))
                    .border(
                        BorderStroke(1.dp, Color(0x35FFFFFF)),
                        RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = text,
                        color = Color(0xFF32215F),
                        fontSize = 14.sp,
                        lineHeight = 23.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}