package com.example.futuris.screens.chat

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar

@Composable
fun ChatScreen(
    firstName: String,
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val safeFirstName = firstName.trim().ifBlank { "User" }
    var message by remember { mutableStateOf("") }

    val tabs = remember {
        listOf(
            BottomTabItem("Home", "home", R.drawable.nav_home),
            BottomTabItem("Chat", "chat", R.drawable.nav_chat),
            BottomTabItem("Alerts", "alerts", R.drawable.nav_alerts),
            BottomTabItem("Profile", "profile", R.drawable.nav_profile)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Background image
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = "Chat background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x18000000),
                            Color(0x12000000),
                            Color(0x30000000)
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
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Futuris AI",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your personal future assistant",
                color = Color(0xFFE3D6F4),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(34.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chat_robot),
                    contentDescription = "Chat robot",
                    modifier = Modifier.size(130.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xAA3A1560))
                    .border(
                        width = 1.dp,
                        color = Color(0x40FFFFFF),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Hello $safeFirstName! Ask me anything\nabout your future.",
                    color = Color.White,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ChatInputBar(
                value = message,
                onValueChange = { message = it },
                onSendClick = {
                    // frontend only for now
                }
            )

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
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xC71D0730),
                        Color(0xCC2B0B45)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Color(0x55FFFFFF)
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 15.sp
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "Ask Futuris something...",
                        color = Color(0xFFBFAFD4),
                        fontSize = 15.sp
                    )
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFB874FF),
                            Color(0xFF8A4DFF)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "➜",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}