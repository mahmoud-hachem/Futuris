package com.example.futuris.screens.alerts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.data.AlertItem
import com.example.futuris.data.AlertMemoryStore
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar
import kotlinx.coroutines.launch

@Composable
fun AlertsScreen(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val tabs = remember {
        listOf(
            BottomTabItem("Home", "home", R.drawable.nav_home),
            BottomTabItem("Chat", "chat", R.drawable.nav_chat),
            BottomTabItem("Alerts", "alerts", R.drawable.nav_alerts),
            BottomTabItem("Profile", "profile", R.drawable.nav_profile)
        )
    }

    val alerts = remember {
        mutableStateListOf<AlertItem>().apply {
            addAll(AlertMemoryStore.getAlerts(context))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = "Alerts background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x22000000),
                            Color(0x30000000),
                            Color(0x44000000)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Future Signals",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your real Futuris activity and insight updates",
                        color = Color(0xFFD6C8F0),
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0x30FFFFFF))
                        .border(
                            width = 1.dp,
                            color = Color(0x35FFFFFF),
                            shape = CircleShape
                        )
                        .clickable {
                            scope.launch {
                                AlertMemoryStore.clearAlerts(context)
                                alerts.clear()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete notifications",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (alerts.isEmpty()) {
                EmptyAlertsState(
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(alerts, key = { it.id }) { alert ->
                        AlertNotificationCard(alert = alert)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassBottomBar(
                selectedTab = currentTab,
                tabs = tabs,
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
private fun EmptyAlertsState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFD98CFF),
                                Color(0xFF8E3DFF)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0x80FFFFFF),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chat_robot),
                    contentDescription = "Bot icon",
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No alerts yet",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "As soon as Futuris generates new insights,\nthis screen will show them here.",
                color = Color(0xFFD8CCE9),
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
fun AlertNotificationCard(alert: AlertItem) {
    val (accentStart, accentEnd, softBackground) = categoryColors(alert.category)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = alert.timeLabel,
            color = Color(0xFFD1C2E8),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xAA240D3D),
                            Color(0xAA2D1248),
                            Color(0xAA240D3D)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0x35FFFFFF),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                accentStart,
                                accentEnd
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0x80FFFFFF),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chat_robot),
                    contentDescription = "Bot icon",
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = alert.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    if (alert.isNew) {
                        Spacer(modifier = Modifier.size(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(softBackground)
                                .border(
                                    width = 1.dp,
                                    color = accentStart.copy(alpha = 0.55f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "NEW",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Futuris: ${alert.message}",
                    color = Color(0xFFF3EEFF),
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

private fun categoryColors(
    category: String
): Triple<Color, Color, Color> {
    return when (category.lowercase()) {
        "love" -> Triple(
            Color(0xFFFF86C8),
            Color(0xFFD84E9F),
            Color(0x33FF86C8)
        )

        "career" -> Triple(
            Color(0xFFC6B8FF),
            Color(0xFF8A63FF),
            Color(0x33C6B8FF)
        )

        "finance" -> Triple(
            Color(0xFFFFD76A),
            Color(0xFFD39B1D),
            Color(0x33FFD76A)
        )

        "mood" -> Triple(
            Color(0xFF89D9FF),
            Color(0xFF4FA5D9),
            Color(0x3389D9FF)
        )

        "decisions" -> Triple(
            Color(0xFF8AF3E0),
            Color(0xFF22C7A9),
            Color(0x338AF3E0)
        )

        "lifepath" -> Triple(
            Color(0xFFC6B8FF),
            Color(0xFF5E60CE),
            Color(0x33C6B8FF)
        )

        else -> Triple(
            Color(0xFFD98CFF),
            Color(0xFF8E3DFF),
            Color(0x33D98CFF)
        )
    }
}