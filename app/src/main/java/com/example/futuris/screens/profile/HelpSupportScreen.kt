package com.example.futuris.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R

private data class FaqItem(val question: String, val answer: String)

@Composable
fun HelpSupportScreen(onBackClick: () -> Unit) {

    val cardFill   = Color(0x8F4B1D76)
    val cardBorder = Color(0x55E3CCFF)
    val subtleText = Color(0xFFE9DDF6)

    val faqs = remember {
        listOf(
            FaqItem(
                "How does Futuris generate predictions?",
                "Futuris combines your quiz answers, chat conversations, zodiac sign, and life focus to build a personal profile. This profile is then used by our AI engine to generate predictions tailored specifically to you."
            ),
            FaqItem(
                "Is my data private and secure?",
                "Yes. Your data is encrypted in transit and stored securely. We never sell your personal information to third parties. You can delete your account and all associated data at any time from Privacy & Security settings."
            ),
            FaqItem(
                "How do I improve my predictions?",
                "The more you interact — completing the quiz, chatting with Futuris AI, and exploring categories — the more personalized your predictions become. We recommend completing all 5 quiz questions and chatting regularly."
            ),
            FaqItem(
                "Can I change my zodiac-based info?",
                "Your zodiac sign is automatically calculated from your date of birth. To update it, you would need to update your date of birth, which is a core field set at signup to protect prediction accuracy."
            ),
            FaqItem(
                "Why is my category prediction not changing?",
                "Predictions update based on new quiz answers and chat activity. Try chatting with Futuris about what's on your mind — the AI engine picks up on keywords and patterns to adjust your scores."
            ),
            FaqItem(
                "How do I reset my quiz answers?",
                "You can retake the Destiny Quiz at any time from the Home screen. Each new submission replaces your old answers, immediately affecting your predictions."
            )
        )
    }

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
                Text("Help & Support", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                // ── Contact cards ──
                item {
                    Text("Contact Us", color = Color(0xFFD4BAFF), fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(cardFill)
                            .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
                    ) {
                        ContactRow(
                            icon = Icons.Outlined.Email,
                            title = "Email Support",
                            subtitle = "futurisesib@gmail.com",
                            onClick = { }
                        )
                    }
                }

                // ── FAQ ──
                item {
                    Text("Frequently Asked Questions", color = Color(0xFFD4BAFF), fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp))
                }

                items(faqs) { faq ->
                    FaqCard(faq = faq, cardFill = cardFill, cardBorder = cardBorder, subtleText = subtleText)
                }

                // ── App version ──
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Futuris", color = Color(0xFFD4BAFF), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Version 1.0.0", color = Color(0x88FFFFFF), fontSize = 12.sp)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun ContactRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
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
            Text(subtitle, color = Color(0xFFE9DDF6), fontSize = 12.sp)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null,
            tint = Color(0x88FFFFFF), modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun FaqCard(
    faq: FaqItem,
    cardFill: Color,
    cardBorder: Color,
    subtleText: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardFill)
            .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                faq.question,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = Color(0xFFD4BAFF),
                modifier = Modifier.size(20.dp).rotate(if (expanded) 180f else 0f)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                Text(
                    faq.answer,
                    color = subtleText,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                )
            }
        }
    }
}