package com.example.futuris.screens.profile

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B0918),
            Color(0xFF15112A),
            Color(0xFF1A1433)
        )
    )

    val cardColor = Color(0xFF19152B)
    val cardColorSoft = Color(0xFF211B38)
    val borderColor = Color.White.copy(alpha = 0.08f)
    val textPrimary = Color(0xFFF7F3FF)
    val textSecondary = Color(0xFFB5AEC9)
    val accentPurple = Color(0xFF9B7BFF)
    val accentGold = Color(0xFFFFD98A)
    val accentBlue = Color(0xFF87C7FF)
    val accentGreen = Color(0xFF8BE9C1)

    fun copyText(label: String, value: String) {
        clipboardManager.setText(AnnotatedString(value))
        Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Help & Support",
                        color = textPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 18.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, RoundedCornerShape(26.dp)),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(26.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF241C43),
                                    Color(0xFF19152B)
                                )
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                Color.White.copy(alpha = 0.07f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = accentGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "We’re here to help",
                        color = textPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Get help with your account, password, email verification, and general Futuris guidance in one clean support space.",
                        color = textSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SupportStatusChip(
                            text = "Real support info",
                            textColor = textPrimary,
                            bgColor = Color.White.copy(alpha = 0.06f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SupportStatusChip(
                            text = "In-app experience",
                            textColor = textPrimary,
                            bgColor = Color(0x229B7BFF)
                        )
                    }
                }
            }

            // CONTACT CENTER
            Text(
                text = "Contact center",
                color = textPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Color.White.copy(alpha = 0.06f),
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = accentPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Official Futuris support",
                                color = textPrimary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Professional assistance and guidance",
                                color = textSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.07f)
                    )

                    ContactInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = "futurisesib@gmail.com",
                        iconTint = accentGold,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        onCopyClick = {
                            copyText("Email", "futurisesib@gmail.com")
                        }
                    )

                    ContactInfoRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = "03423655",
                        iconTint = accentBlue,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        onCopyClick = {
                            copyText("Phone number", "03423655")
                        }
                    )

                    ContactInfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Headquarters",
                        value = "USJ ESIB",
                        iconTint = accentGreen,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        onCopyClick = null
                    )

                    ContactInfoRow(
                        icon = Icons.Default.Schedule,
                        label = "Support hours",
                        value = "Monday - Friday | 9:00 AM - 5:00 PM",
                        iconTint = accentPurple,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        onCopyClick = null
                    )

                    ContactInfoRow(
                        icon = Icons.Default.HelpCenter,
                        label = "Response time",
                        value = "Usually within 24 hours",
                        iconTint = accentGold,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        onCopyClick = null
                    )
                }
            }

            // SUPPORT SECTIONS
            Text(
                text = "Support sections",
                color = textPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SupportSectionCard(
                    title = "Account access",
                    subtitle = "Help with login problems, account verification, and profile access.",
                    icon = Icons.Default.VerifiedUser,
                    iconTint = accentPurple,
                    cardColor = cardColorSoft,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                SupportSectionCard(
                    title = "Password assistance",
                    subtitle = "Guidance for password reset, update issues, and credential support.",
                    icon = Icons.Default.Lock,
                    iconTint = accentBlue,
                    cardColor = cardColorSoft,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                SupportSectionCard(
                    title = "General guidance",
                    subtitle = "Understand how Futuris works and how to improve your app experience.",
                    icon = Icons.Default.HelpCenter,
                    iconTint = accentGold,
                    cardColor = cardColorSoft,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }

            // FAQ
            Text(
                text = "Frequently asked questions",
                color = textPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            FAQItem(
                question = "How do I get better insights?",
                answer = "Complete your quiz, verify your email, interact naturally in chat, and use the categories regularly. The more real input Futuris receives, the more personalized the experience becomes.",
                cardColor = cardColor,
                borderColor = borderColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )

            FAQItem(
                question = "What does Futuris use to personalize my experience?",
                answer = "Futuris uses your signup details, quiz answers, chat interactions, and category activity inside the app to build more relevant guidance and insights.",
                cardColor = cardColor,
                borderColor = borderColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )

            FAQItem(
                question = "What should I do if I can’t log in?",
                answer = "First check your connection, verify your credentials, and try again. If the issue continues, use the support contact information shown on this page.",
                cardColor = cardColor,
                borderColor = borderColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )

            FAQItem(
                question = "What if my password or email is not working?",
                answer = "Use the account options available in the app. If the problem remains, contact Futuris support using the official email or phone listed in this support center.",
                cardColor = cardColor,
                borderColor = borderColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )

            FAQItem(
                question = "When can I expect a reply?",
                answer = "Support requests are usually reviewed within 24 hours during official support hours.",
                cardColor = cardColor,
                borderColor = borderColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )

            // APP INFO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Support information",
                        color = textPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    SupportMiniInfoRow(
                        icon = Icons.Default.HelpCenter,
                        title = "Guidance",
                        subtitle = "Help for app use, account access, and support questions",
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )

                    SupportMiniInfoRow(
                        icon = Icons.Default.Lock,
                        title = "Privacy-aware",
                        subtitle = "Support is focused on your Futuris app experience",
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )

                    SupportMiniInfoRow(
                        icon = Icons.Default.PrivacyTip,
                        title = "Official contact",
                        subtitle = "Email, phone, and headquarters are shown directly inside the app",
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.07f)
                    )

                    Text(
                        text = "App version",
                        color = textSecondary,
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = "Futuris v1.0.0",
                        color = textPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.size(6.dp))
        }
    }
}

@Composable
private fun SupportStatusChip(
    text: String,
    textColor: Color,
    bgColor: Color
) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ContactInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    textPrimary: Color,
    textSecondary: Color,
    onCopyClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color.White.copy(alpha = 0.06f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                color = textSecondary,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = value,
                color = textPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (onCopyClick != null) {
            IconButton(onClick = onCopyClick) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = textSecondary
                )
            }
        }
    }
}

@Composable
private fun SupportSectionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        Color.White.copy(alpha = 0.06f),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = textPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun FAQItem(
    question: String,
    answer: String,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.QuestionAnswer,
                    contentDescription = null,
                    tint = Color(0xFFFFD98A),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = question,
                    color = textPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = textSecondary
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.06f)
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = answer,
                        color = textSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun SupportMiniInfoRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    Color.White.copy(alpha = 0.06f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF87C7FF),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                color = textPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}