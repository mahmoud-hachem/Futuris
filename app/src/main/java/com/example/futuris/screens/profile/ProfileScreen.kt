package com.example.futuris.screens.profile

import android.graphics.BitmapFactory
import android.net.Uri
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar
import com.example.futuris.utils.getZodiacSign
import java.io.InputStream

// ── Color tokens ──
private val CardFill   = Color(0x8F4B1D76)
private val CardBorder = Color(0x55E3CCFF)
private val CardIcon   = Color(0xFFF1DDFF)
private val SubtleText = Color(0xFFE9DDF6)
private val AvatarBorder = Color(0xFFC992FF)
private val AvatarGlow   = Color(0x667F4CFF)
private val LogoutFill   = Color(0xB0491039)
private val LogoutBorder = Color(0x66FFB7D1)
private val StatBg       = Color(0x33FFFFFF)

@Composable
fun ProfileScreen(
    firstName: String,
    lastName: String,
    email: String,
    dateOfBirth: String = "",
    gender: String = "",
    username: String = "",
    lifeFocus: String = "",
    currentTab: String,
    onTabSelected: (String) -> Unit,
    onOpenAccountInformation: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onOpenHelp: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    val fullName    = "${firstName.trim()} ${lastName.trim()}".trim().ifBlank { "Futuris User" }
    val safeEmail   = email.trim().ifBlank { "email@example.com" }
    val safeUsername = username.trim().ifBlank { firstName.trim() }

    // Zodiac from DOB
    val zodiac = remember(dateOfBirth) {
        if (dateOfBirth.isNotBlank()) getZodiacSign(dateOfBirth) else "Unknown"
    }

    // Initials for avatar fallback
    val initials = buildString {
        if (firstName.isNotBlank()) append(firstName.trim().first().uppercaseChar())
        if (lastName.isNotBlank())  append(lastName.trim().first().uppercaseChar())
        if (isEmpty() && safeUsername.isNotBlank()) append(safeUsername.first().uppercaseChar())
        if (isEmpty()) append("F")
    }

    var showAvatarDialog by remember { mutableStateOf(false) }
    var profileImage by rememberSaveable { mutableStateOf<ImageBitmap?>(null) }

    val tabs = remember {
        listOf(
            BottomTabItem("Home",    "home",    R.drawable.nav_home),
            BottomTabItem("Chat",    "chat",    R.drawable.nav_chat),
            BottomTabItem("Alerts",  "alerts",  R.drawable.nav_alerts),
            BottomTabItem("Profile", "profile", R.drawable.nav_profile)
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap -> if (bitmap != null) profileImage = bitmap.asImageBitmap() }

    val cameraPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) cameraLauncher.launch(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> if (uri != null) profileImage = loadImageBitmapFromUri(context, uri) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color(0x14000000), Color(0x1C000000), Color(0x32000000)))
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text("Profile", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ── Avatar + name + stats row ──
                item {
                    ProfileHeaderCard(
                        fullName    = fullName,
                        email       = safeEmail,
                        username    = safeUsername,
                        zodiac      = zodiac,
                        gender      = gender,
                        lifeFocus   = lifeFocus,
                        initials    = initials,
                        profileImage = profileImage,
                        onAvatarClick = { showAvatarDialog = true }
                    )
                }

                // ── Menu options ──
                item {
                    ProfileOptionCard(
                        title    = "Account Information",
                        subtitle = "Edit name, email & username",
                        icon     = Icons.Outlined.PersonOutline,
                        onClick  = onOpenAccountInformation
                    )
                }
                item {
                    ProfileOptionCard(
                        title    = "Notification Preferences",
                        subtitle = "Control reminders and updates",
                        icon     = Icons.Outlined.NotificationsNone,
                        onClick  = onOpenNotifications
                    )
                }
                item {
                    ProfileOptionCard(
                        title    = "Privacy & Security",
                        subtitle = "Keep your account protected",
                        icon     = Icons.Outlined.Shield,
                        onClick  = onOpenPrivacy
                    )
                }
                item {
                    ProfileOptionCard(
                        title    = "Help & Support",
                        subtitle = "Get help with your Futuris account",
                        icon     = Icons.Outlined.HelpOutline,
                        onClick  = onOpenHelp
                    )
                }

                // ── Logout ──
                item { LogoutCard(onLogoutClick = onLogout) }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassBottomBar(
                selectedTab  = currentTab,
                tabs         = tabs,
                onTabSelected = onTabSelected
            )
        }
    }

    // Avatar picker dialog
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            containerColor = Color(0xFF241136),
            title = {
                Text("Update profile photo", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AvatarChoiceRow(
                        icon  = Icons.Outlined.CameraAlt,
                        title = "Take photo",
                        onClick = {
                            showAvatarDialog = false
                            cameraPermLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                    AvatarChoiceRow(
                        icon  = Icons.Outlined.PhotoLibrary,
                        title = "Choose from gallery",
                        onClick = {
                            showAvatarDialog = false
                            galleryLauncher.launch("image/*")
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text("Close", color = Color(0xFFD8B8FF))
                }
            }
        )
    }
}

// ─────────────────────────────────────────
// Profile Header Card — avatar + name + stat pills
// ─────────────────────────────────────────
@Composable
private fun ProfileHeaderCard(
    fullName: String,
    email: String,
    username: String,
    zodiac: String,
    gender: String,
    lifeFocus: String,
    initials: String,
    profileImage: ImageBitmap?,
    onAvatarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(CardFill)
            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(28.dp))
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

            // Avatar
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(AvatarGlow, Color(0x33FFFFFF), Color.Transparent))),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0x66F3EAFF))
                        .border(2.dp, AvatarBorder, CircleShape)
                        .clickable { onAvatarClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImage != null) {
                        Image(
                            bitmap = profileImage,
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Text(
                            text = initials,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // Camera badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 2.dp, bottom = 2.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB15CFF))
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(fullName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(2.dp))

            Text("@$username", color = Color(0xFFD4BAFF), fontSize = 13.sp)

            Spacer(modifier = Modifier.height(2.dp))

            Text(email, color = SubtleText, fontSize = 12.sp)

            if ((zodiac.isNotBlank() && zodiac != "Unknown") || gender.isNotBlank() || lifeFocus.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                // ── Stat pills ──
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (zodiac.isNotBlank() && zodiac != "Unknown") {
                        StatPill(label = "Zodiac", value = zodiac, modifier = Modifier.weight(1f))
                    }
                    if (gender.isNotBlank()) {
                        StatPill(label = "Gender", value = gender, modifier = Modifier.weight(1f))
                    }
                    if (lifeFocus.isNotBlank()) {
                        StatPill(label = "Focus", value = lifeFocus, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(StatBg)
            .border(BorderStroke(1.dp, Color(0x44FFFFFF)), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(2.dp))
        Text(label, color = SubtleText, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}

// ─────────────────────────────────────────
// Menu option row
// ─────────────────────────────────────────
@Composable
private fun ProfileOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(CardFill)
            .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(CircleShape).background(Color(0x26FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CardIcon, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = SubtleText, fontSize = 12.sp)
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Color(0x88FFFFFF),
            modifier = Modifier.size(20.dp)
        )
    }
}

// ─────────────────────────────────────────
// Logout button
// ─────────────────────────────────────────
@Composable
private fun LogoutCard(onLogoutClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(LogoutFill)
            .border(BorderStroke(1.dp, LogoutBorder), RoundedCornerShape(22.dp))
            .clickable { onLogoutClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Outlined.Logout, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text("Log Out", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

// ─────────────────────────────────────────
// Avatar dialog row
// ─────────────────────────────────────────
@Composable
private fun AvatarChoiceRow(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x8F4B1D76))
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = CardIcon, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}

private fun loadImageBitmapFromUri(context: android.content.Context, uri: Uri): ImageBitmap? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        bitmap?.asImageBitmap()
    } catch (_: Exception) { null }
}