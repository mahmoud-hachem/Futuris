package com.example.futuris.screens.profile

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
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                            onClick = { }
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.07f), modifier = Modifier.padding(horizontal = 16.dp))
                        PrivacyActionRow(
                            icon = Icons.Outlined.Gavel,
                            title = "Terms of Service",
                            subtitle = "Rules and conditions of use",
                            onClick = { }
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