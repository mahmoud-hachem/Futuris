package com.example.futuris.screens.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Shield
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar

@Composable
fun ProfileScreen(
    firstName: String,
    lastName: String,
    email: String,
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val fullName = "${firstName.trim()} ${lastName.trim()}".trim().ifBlank { "User Name" }
    val safeEmail = email.trim().ifBlank { "email@example.com" }

    val tabs = remember {
        listOf(
            BottomTabItem("Home", "home", R.drawable.nav_home),
            BottomTabItem("Chat", "chat", R.drawable.nav_chat),
            BottomTabItem("Alerts", "alerts", R.drawable.nav_alerts),
            BottomTabItem("Profile", "profile", R.drawable.nav_profile)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = "Profile background",
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
                            Color(0x15000000),
                            Color(0x28000000)
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
            Text(
                text = "Profile",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    ProfileHeader(
                        fullName = fullName,
                        email = safeEmail
                    )
                }

                item {
                    LinkAccountsCard()
                }

                item {
                    ProfileOptionCard(
                        title = "Account Settings",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.PersonOutline,
                                contentDescription = "Account Settings",
                                tint = Color(0xFF1F1A29),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    )
                }

                item {
                    ProfileOptionCard(
                        title = "Notification Preferences",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.NotificationsNone,
                                contentDescription = "Notification Preferences",
                                tint = Color(0xFF1F1A29),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    )
                }

                item {
                    ProfileOptionCard(
                        title = "Privacy & Security",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Shield,
                                contentDescription = "Privacy & Security",
                                tint = Color(0xFF1F1A29),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    )
                }

                item {
                    ProfileOptionCard(
                        title = "Help & Support",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.HelpOutline,
                                contentDescription = "Help & Support",
                                tint = Color(0xFF1F1A29),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    )
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
fun ProfileHeader(
    fullName: String,
    email: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.size(105.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(95.dp)
                    .clip(CircleShape)
                    .background(Color(0x55FFFFFF))
                    .border(
                        width = 3.dp,
                        color = Color(0xFFB15CFF),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB15CFF))
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📷",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = fullName,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = email,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LinkAccountsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xA03E1663))
            .border(
                width = 1.dp,
                color = Color(0x35FFFFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Text(
            text = "Link your accounts",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialIcon(R.drawable.ic_instagram, "Instagram")
            SocialIcon(R.drawable.ic_facebook, "Facebook")
            SocialIcon(R.drawable.ic_x, "X")
        }
    }
}

@Composable
fun SocialIcon(
    drawableRes: Int,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun ProfileOptionCard(
    title: String,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xA03E1663))
            .border(
                width = 1.dp,
                color = Color(0x35FFFFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}