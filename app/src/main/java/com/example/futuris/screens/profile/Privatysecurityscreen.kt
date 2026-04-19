package com.example.futuris.screens.profile

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
fun PrivacySecurityScreen(
    onBackClick: () -> Unit,
    onChangePassword: () -> Unit
) {
    var dataAnalytics   by remember { mutableStateOf(true) }
    var personalization by remember { mutableStateOf(true) }
    var showDeleteDialog  by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog   by remember { mutableStateOf(false) }

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
                Text("Privacy & Security", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                // ── Security section ──
                item {
                    PrivacySectionLabel("Security", subtleText = Color(0xFFD4BAFF))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(cardFill)
                            .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
                    ) {
                        PrivacyActionRow(
                            icon = Icons.Outlined.Lock,
                            title = "Change Password",
                            subtitle = "Update your account password",
                            onClick = onChangePassword
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.07f), modifier = Modifier.padding(horizontal = 16.dp))
                        PrivacyActionRow(
                            icon = Icons.Outlined.Security,
                            title = "Two-Factor Authentication",
                            subtitle = "Add an extra layer of security",
                            badge = "Coming soon",
                            onClick = { }
                        )
                    }
                }

                // ── Data & Privacy section ──
                item {
                    PrivacySectionLabel("Data & Privacy", subtleText = Color(0xFFD4BAFF))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(cardFill)
                            .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        PrivacyToggleRow(
                            icon = Icons.Outlined.Analytics,
                            title = "Usage Analytics",
                            subtitle = "Help improve Futuris with anonymous data",
                            checked = dataAnalytics,
                            onCheckedChange = { dataAnalytics = it },
                            subtleText = subtleText
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                        PrivacyToggleRow(
                            icon = Icons.Outlined.AutoAwesome,
                            title = "Personalization",
                            subtitle = "Use your data to improve predictions",
                            checked = personalization,
                            onCheckedChange = { personalization = it },
                            subtleText = subtleText
                        )
                    }
                }

                // ── Legal section ──
                item {
                    PrivacySectionLabel("Legal", subtleText = Color(0xFFD4BAFF))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(cardFill)
                            .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
                    ) {
                        PrivacyActionRow(
                            icon = Icons.Outlined.Description,
                            title = "Privacy Policy",
                            subtitle = "How we handle your data",
                            onClick = { showPrivacyDialog = true }
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.07f), modifier = Modifier.padding(horizontal = 16.dp))
                        PrivacyActionRow(
                            icon = Icons.Outlined.Gavel,
                            title = "Terms of Service",
                            subtitle = "Rules and conditions of use",
                            onClick = { showTermsDialog = true }
                        )
                    }
                }

                // ── Danger zone ──
                item {
                    PrivacySectionLabel("Danger Zone", subtleText = Color(0xFFFF9090))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xB0491039))
                            .border(1.dp, Color(0x66FF6B6B), RoundedCornerShape(20.dp))
                            .clickable { showDeleteDialog = true }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp))
                                .background(Color(0x33FF6B6B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.DeleteForever, contentDescription = null,
                                tint = Color(0xFFFF9090), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Delete Account", color = Color(0xFFFF9090),
                                fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Permanently remove your data", color = Color(0xFFFFB0B0), fontSize = 12.sp)
                        }
                        Icon(Icons.Outlined.ChevronRight, contentDescription = null,
                            tint = Color(0x88FF9090), modifier = Modifier.size(20.dp))
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }

    // Delete account confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color(0xFF241136),
            title = { Text("Delete Account?", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will permanently delete your account and all your data. This action cannot be undone.",
                    color = Color(0xFFE9DDF6), fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Delete", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color(0xFFD8B8FF))
                }
            }
        )
    }

    // Privacy Policy dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            containerColor = Color(0xFF1C1030),
            title = {
                Text("Privacy Policy", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PolicySection("1. Data We Collect",
                        "Futuris collects personal information you provide during registration (name, email, date of birth, gender), your quiz answers, chat messages, and app usage patterns. This data is used exclusively to generate personalized predictions and improve your experience.")
                    PolicySection("2. How We Use Your Data",
                        "Your data is used to generate AI-driven predictions, personalize your insights across categories, and improve the accuracy of our prediction engine. We do not use your data for advertising purposes.")
                    PolicySection("3. Data Storage & Security",
                        "All data is encrypted in transit using TLS and stored securely on our servers. We follow ISO/IEC 27701 standards for privacy information management. Access to your data is restricted to authorized systems only.")
                    PolicySection("4. Third-Party Sharing",
                        "Futuris does not sell, rent, or share your personal data with third parties for marketing purposes. We may use trusted service providers (such as cloud infrastructure) who process data solely on our behalf.")
                    PolicySection("5. Your Rights",
                        "You have the right to access, correct, or delete your personal data at any time. You may request account deletion from the Privacy & Security settings. Deletion requests are processed within 30 days.")
                    PolicySection("6. Contact",
                        "For privacy-related inquiries, contact us at: futurisesib@gmail.com")
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Close", color = Color(0xFFD8B8FF), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    // Terms of Service dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            containerColor = Color(0xFF1C1030),
            title = {
                Text("Terms of Service", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PolicySection("1. Acceptance of Terms",
                        "By using Futuris, you agree to these Terms of Service. If you do not agree, please do not use the application. These terms apply to all users of the Futuris mobile application.")
                    PolicySection("2. Nature of Predictions",
                        "Futuris provides AI-generated insights and predictions for entertainment and self-reflection purposes only. Predictions are probabilistic and should not be used as the sole basis for major life decisions.")
                    PolicySection("3. User Responsibilities",
                        "You are responsible for maintaining the confidentiality of your account credentials. You agree not to misuse the platform, attempt to reverse-engineer the AI, or provide false information during registration.")
                    PolicySection("4. Intellectual Property",
                        "All content, design, and AI systems within Futuris are the intellectual property of the Futuris team. You may not copy, distribute, or modify any part of the application without prior written consent.")
                    PolicySection("5. Limitation of Liability",
                        "Futuris is provided as-is. We are not liable for any decisions made based on the predictions generated by the application. Use of the app is at your own discretion and risk.")
                    PolicySection("6. Changes to Terms",
                        "We reserve the right to update these terms at any time. Continued use of the app after changes constitutes acceptance of the new terms.")
                    PolicySection("7. Contact",
                        "For terms-related inquiries, contact us at: futurisesib@gmail.com")
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Close", color = Color(0xFFD8B8FF), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

@Composable
private fun PrivacySectionLabel(title: String, subtleText: Color) {
    Text(
        title, color = subtleText, fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
}

@Composable
private fun PrivacyActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String? = null,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                if (badge != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        badge,
                        color = Color(0xFFFFD98A),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x33FFD98A))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(subtitle, color = Color(0xFFE9DDF6), fontSize = 12.sp)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null,
            tint = Color(0x88FFFFFF), modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun PrivacyToggleRow(
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
@Composable
private fun PolicySection(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Text(body, color = Color(0xFFE9DDF6), fontSize = 12.sp, lineHeight = 18.sp)
    }
}