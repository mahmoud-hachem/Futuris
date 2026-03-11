package com.example.futuris.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
            contentDescription = "Home background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x33000000),
                            Color(0x22000000),
                            Color(0x44000000)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Hello, $safeFirstName🔮",
                color = Color.White,
                fontSize = 27.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "What would you like to explore today?",
                color = Color(0xFFE7DDF5),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 18.dp)
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

            FuturisBottomBar(
                selectedTab = "home",
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
                .aspectRatio(1f)
                .clip(RoundedCornerShape(22.dp))
                .clickable { onClick() }
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
                                Color(0x14000000),
                                Color(0x26000000),
                                Color(0x3A000000)
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FuturisBottomBar(
    selectedTab: String,
    tabs: List<BottomTabItem>,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xCC53127A),
                        Color(0xCC2A063D)
                    )
                )
            )
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            BottomBarItem(
                label = tab.label,
                iconRes = tab.iconRes,
                selected = selectedTab == tab.key,
                onClick = { onTabSelected(tab.key) }
            )
        }
    }
}

@Composable
fun BottomBarItem(
    label: String,
    @DrawableRes imageRes: Int? = null,
    @DrawableRes iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = Color(0xFFF2B4FF)
    val inactiveColor = Color(0xFFD6B8EC)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(activeColor)
                )
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(22.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = if (selected) activeColor else inactiveColor,
            fontSize = 12.sp
        )
    }
}