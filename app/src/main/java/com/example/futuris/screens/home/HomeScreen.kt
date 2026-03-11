package com.example.futuris.screens.home

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R

data class HomeCategory(
    val title: String,
    val key: String,
    @param:DrawableRes val imageRes: Int
)

data class BottomTabItem(
    val label: String,
    val key: String,
    @param:DrawableRes val iconRes: Int
)

@Composable
fun HomeScreen(
    firstName: String,
    currentTab: String,
    onCategoryClick: (String) -> Unit,
    onTabSelected: (String) -> Unit
) {
    val safeFirstName = firstName.trim().ifBlank { "User" }

    val categories = remember {
        listOf(
            HomeCategory("Love & Relationships", "love", R.drawable.card_love),
            HomeCategory("Career & Studies", "career", R.drawable.card_career),
            HomeCategory("Money", "money", R.drawable.card_money),
            HomeCategory("Finance", "finance", R.drawable.card_finance),
            HomeCategory("Social & Family", "social", R.drawable.card_social),
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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = "Background",
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
                            Color(0x18000000),
                            Color(0x35000000)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 18.dp)
                .padding(top = 24.dp, bottom = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Hello, $safeFirstName🔮",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "What would you like to explore today?",
                color = Color(0xFFF0E5FA),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 6.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        title = category.title,
                        imageRes = category.imageRes,
                        onClick = { onCategoryClick(category.key) }
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
                .aspectRatio(1.28f)
                .clip(RoundedCornerShape(20.dp))
                .clickable { onClick() }
                .background(Color(0x20000000))
                .border(
                    width = 1.dp,
                    color = Color(0x30FFFFFF),
                    shape = RoundedCornerShape(20.dp)
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
                            colors = listOf(
                                Color.Transparent,
                                Color(0x12000000),
                                Color(0x28000000)
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
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
            .navigationBarsPadding()
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x55FFFFFF),
                        Color(0x22FFFFFF)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0x90F2B6FF),
                            Color(0x50FFFFFF),
                            Color(0x70C77DFF)
                        )
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x883A0B57),
                        Color(0xAA1A0327)
                    )
                )
            )
            .padding(horizontal = 6.dp, vertical = 5.dp)
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
    val activeColor = Color(0xFFF2B2FF)
    val inactiveColor = Color(0xFFD6C1E8)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0x55F1A8FF),
                                    Color.Transparent
                                )
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
            fontSize = 12.sp
        )
    }
}