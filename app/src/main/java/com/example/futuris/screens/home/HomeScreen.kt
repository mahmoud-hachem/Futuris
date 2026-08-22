package com.example.futuris.screens.home

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.futuris.data.OnboardingStateManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HomeCategory(
    val title: String,
    val key: String,
    @DrawableRes val imageRes: Int
)

data class BottomTabItem(
    val label: String,
    val key: String,
    @DrawableRes val iconRes: Int
)

@Composable
fun HomeScreen(
    firstName: String,
    currentTab: String,
    onCategoryClick: (String) -> Unit,
    onTabSelected: (String) -> Unit,
    onDestinyQuizClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val prefs = remember {
        context.getSharedPreferences("FuturisPrefs", Context.MODE_PRIVATE)
    }

    val userId = remember {
        prefs.getString("userId", "")?.trim().orEmpty()
            .ifBlank { "default_user" }
    }

    val isQuizCompleted = remember(userId) {
        OnboardingStateManager.isOnboardingFinished(
            context = context,
            userId = userId
        )
    }

    val safeFirstName = firstName.trim().ifBlank { "Alex" }

    val categories = remember {
        listOf(
            HomeCategory("Love & Relationships", "love", R.drawable.card_love),
            HomeCategory("Career & Studies", "career", R.drawable.card_career),
            HomeCategory("Finance", "finance", R.drawable.card_finance),
            HomeCategory("Mood & Energy", "mood", R.drawable.card_mood),
            HomeCategory("Decisions & Guidance", "decisions", R.drawable.card_decisions),
            HomeCategory("Life Path", "lifepath", R.drawable.card_lifepath)
        )
    }

    val tabs = remember {
        listOf(
            BottomTabItem("Home", "home", R.drawable.nav_home),
            BottomTabItem("Chat", "chat", R.drawable.nav_chat),
            BottomTabItem("Alerts", "alerts", R.drawable.nav_alerts),
            BottomTabItem("Profile", "profile", R.drawable.nav_profile)
        )
    }

    LaunchedEffect(Unit) {
        val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastDailyAlertDate = prefs.getString("last_daily_alert_date", "") ?: ""

        if (lastDailyAlertDate != todayKey) {
            AlertMemoryStore.addAlert(
                context = context,
                alert = AlertItem(
                    id = System.currentTimeMillis().toString(),
                    title = "New Daily Signals",
                    message = "Fresh predictions are waiting for you.",
                    timeLabel = "Now",
                    category = "system",
                    isNew = true
                )
            )

            prefs.edit().putString("last_daily_alert_date", todayKey).apply()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val greeting = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
                in 5..11 -> "Good morning"
                in 12..17 -> "Good afternoon"
                else -> "Good evening"
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$greeting, $safeFirstName 🔮",
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Discover what awaits you",
                color = Color(0xFFE8D8F8),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        title = category.title,
                        imageRes = category.imageRes,
                        onClick = { onCategoryClick(category.key) }
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    DestinyQuizCard(
                        onClick = onDestinyQuizClick,
                        isQuizCompleted = isQuizCompleted
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            GlassBottomBar(
                selectedTab = currentTab,
                tabs = tabs,
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(18.dp))
                .clickable { onClick() }
                .border(
                    width = 1.dp,
                    color = Color(0x44FFFFFF),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0x33000000))
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 17.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
fun DestinyQuizCard(
    onClick: () -> Unit,
    isQuizCompleted: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x772E0C52),
                        Color(0x99310C56)
                    )
                )
            )
            .border(
                BorderStroke(1.dp, Color(0x88C68CFF)),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(enabled = !isQuizCompleted) {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.destiny),
                    contentDescription = "Destiny Quiz",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Destiny Quiz",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isQuizCompleted) {
                            "Your core signals are already unlocked.\nNo need to retake the quiz."
                        } else {
                            "Let Futuris guide your future\nthrough questions"
                        },
                        color = Color(0xFFE6D7F6),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isQuizCompleted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF54505F),
                                    Color(0xFF6B6577)
                                )
                            )
                        )
                        .border(
                            BorderStroke(1.dp, Color(0x66FFFFFF)),
                            RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔒 Quiz Completed",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7B4BD0),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text(
                        text = "Start Prediction",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun GlassBottomBar(
    selectedTab: String,
    tabs: List<BottomTabItem>,
    onTabSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0x663B0B57), Color(0x9920032E))
                )
            )
            .border(
                BorderStroke(1.dp, Color(0x55C68CFF)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                GlassBottomBarItem(
                    label = tab.label,
                    iconRes = tab.iconRes,
                    selected = selectedTab == tab.key,
                    onClick = { onTabSelected(tab.key) }
                )
            }
        }
    }
}

@Composable
fun GlassBottomBarItem(
    label: String,
    @DrawableRes iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = Color(0xFFF2A8FF)
    val inactiveColor = Color(0xFFD6C1E8)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0x66F1A8FF), Color.Transparent)
                            )
                        )
                )
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(22.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    if (selected) activeColor else inactiveColor
                )
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            color = if (selected) activeColor else inactiveColor,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}