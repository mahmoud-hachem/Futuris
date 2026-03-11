package com.example.futuris.screens.alerts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar

data class AlertItem(
    val timeLabel: String,
    val message: String
)

@Composable
fun AlertsScreen(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = remember {
        listOf(
            BottomTabItem("Home", "home", R.drawable.nav_home),
            BottomTabItem("Chat", "chat", R.drawable.nav_chat),
            BottomTabItem("Alerts", "alerts", R.drawable.nav_alerts),
            BottomTabItem("Profile", "profile", R.drawable.nav_profile)
        )
    }

    // Placeholder sample data for frontend only
    val alerts = remember {
        listOf(
            AlertItem(
                timeLabel = "Today  10:45 AM",
                message = "Your daily prediction is ready."
            ),
            AlertItem(
                timeLabel = "Yesterday  3:30 PM",
                message = "Your daily prediction is ready."
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            Color(0x12000000),
                            Color(0x16000000),
                            Color(0x26000000)
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
                Text(
                    text = "Notifications",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete notifications",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(alerts) { alert ->
                    AlertNotificationCard(alert = alert)
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
fun AlertNotificationCard(alert: AlertItem) {
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
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xA63A1560))
                .border(
                    width = 1.dp,
                    color = Color(0x35FFFFFF),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
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
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = "Futuris: ${alert.message}",
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 21.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}