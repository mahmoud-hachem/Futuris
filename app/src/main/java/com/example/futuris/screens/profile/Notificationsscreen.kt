package com.example.futuris.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R

@Composable
fun NotificationsScreen(
    notificationsEnabled: Boolean,
    insightReminders: Boolean,
    onBackClick: () -> Unit,
    onSave: (notificationsEnabled: Boolean, insightReminders: Boolean) -> Unit
) {
    var notifEnabled    by remember { mutableStateOf(notificationsEnabled) }
    var insightRemind   by remember { mutableStateOf(insightReminders) }
    var dailyPrediction by remember { mutableStateOf(true) }
    var weeklyReport    by remember { mutableStateOf(false) }
    var chatReplies     by remember { mutableStateOf(true) }
    var quizReminders   by remember { mutableStateOf(true) }

    val cardFill   = Color(0x8F4B1D76)
    val cardBorder = Color(0x55E3CCFF)
    val subtleText = Color(0xFFE9DDF6)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color(0x22000000), Color(0x44000000)))
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // ── Top bar ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 14.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("Notification Preferences", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                item {
                    NotifSectionCard(title = "General", cardFill = cardFill, cardBorder = cardBorder) {
                        NotifToggleRow(
                            icon = Icons.Outlined.Notifications,
                            title = "All Notifications",
                            subtitle = "Master switch for all alerts",
                            checked = notifEnabled,
                            onCheckedChange = { notifEnabled = it },
                            subtleText = subtleText
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                        NotifToggleRow(
                            icon = Icons.Outlined.Lightbulb,
                            title = "Insight Reminders",
                            subtitle = "Remind me to check my predictions",
                            checked = insightRemind,
                            onCheckedChange = { insightRemind = it },
                            subtleText = subtleText
                        )
                    }
                }

                item {
                    NotifSectionCard(title = "Daily & Weekly", cardFill = cardFill, cardBorder = cardBorder) {
                        NotifToggleRow(
                            icon = Icons.Outlined.AutoAwesome,
                            title = "Daily Prediction",
                            subtitle = "Get your daily fortune each morning",
                            checked = dailyPrediction,
                            onCheckedChange = { dailyPrediction = it },
                            subtleText = subtleText
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                        NotifToggleRow(
                            icon = Icons.Outlined.BarChart,
                            title = "Weekly Summary",
                            subtitle = "A recap of your weekly insights",
                            checked = weeklyReport,
                            onCheckedChange = { weeklyReport = it },
                            subtleText = subtleText
                        )
                    }
                }

                item {
                    NotifSectionCard(title = "Activity", cardFill = cardFill, cardBorder = cardBorder) {
                        NotifToggleRow(
                            icon = Icons.Outlined.Chat,
                            title = "Chat Replies",
                            subtitle = "When Futuris AI responds to your message",
                            checked = chatReplies,
                            onCheckedChange = { chatReplies = it },
                            subtleText = subtleText
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                        NotifToggleRow(
                            icon = Icons.Outlined.Quiz,
                            title = "Quiz Reminders",
                            subtitle = "Remind me to complete my personality quiz",
                            checked = quizReminders,
                            onCheckedChange = { quizReminders = it },
                            subtleText = subtleText
                        )
                    }
                }

                item {
                    Button(
                        onClick = { onSave(notifEnabled, insightRemind) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7B4BD0),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save Preferences", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun NotifSectionCard(
    title: String,
    cardFill: Color,
    cardBorder: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(title, color = Color(0xFFD4BAFF), fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 4.dp, bottom = 6.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(cardFill)
                .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            content = content
        )
    }
}

@Composable
private fun NotifToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtleText: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp))
                .background(Color(0x26FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null,
                tint = Color(0xFFF1DDFF), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = subtleText, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF7B4BD0),
                uncheckedThumbColor = Color(0xFFB0A0C8),
                uncheckedTrackColor = Color(0x33FFFFFF)
            )
        )
    }
}