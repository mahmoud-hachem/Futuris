package com.example.futuris.screens.profile

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.io.InputStream

data class AccountInfoUiState(
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val notificationsEnabled: Boolean = true,
    val insightReminders: Boolean = true,
    val profileImageUri: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInformationScreen(
    modifier: Modifier = Modifier,
    userData: AccountInfoUiState,
    onBackClick: () -> Unit = {},
    onSaveClick: (AccountInfoUiState) -> Unit = {},
    onChangePasswordClick: () -> Unit = {}
) {
    val context = LocalContext.current

    var firstName by rememberSaveable(userData.firstName) { mutableStateOf(userData.firstName) }
    var lastName by rememberSaveable(userData.lastName) { mutableStateOf(userData.lastName) }
    var username by rememberSaveable(userData.username) { mutableStateOf(userData.username) }
    var profileImageUri by rememberSaveable(userData.profileImageUri) { mutableStateOf(userData.profileImageUri) }

    val profileImageBitmap = remember(profileImageUri) {
        if (profileImageUri.isBlank()) null
        else loadAccountImageBitmapFromUri(context, Uri.parse(profileImageUri))
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageUri = uri.toString()
        }
    }

    val dateOfBirthDisplay = userData.dateOfBirth.ifBlank { "Not available" }
    val genderDisplay = userData.gender.ifBlank { "Not available" }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D0B1F),
            Color(0xFF17112D),
            Color(0xFF1F1639)
        )
    )

    val cardColor = Color(0xFF1C1730)
    val fieldColor = Color(0xFF241D3F)
    val softPurple = Color(0xFF9D7BFF)
    val softGold = Color(0xFFFFD98A)
    val borderColor = Color(0x33FFFFFF)
    val textPrimary = Color(0xFFF6F2FF)
    val textSecondary = Color(0xFFB7AFCB)
    val accentGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF7D5CFF),
            Color(0xFFB08CFF)
        )
    )

    val displayFullName = listOf(firstName.trim(), lastName.trim())
        .filter { it.isNotEmpty() }
        .joinToString(" ")
        .ifEmpty {
            if (username.isNotBlank()) username else "Futuris User"
        }

    val displayUsername = if (username.isNotBlank()) "@$username" else "@futuris_user"

    val initials = buildString {
        if (firstName.isNotBlank()) append(firstName.trim().first().uppercaseChar())
        if (lastName.isNotBlank()) append(lastName.trim().first().uppercaseChar())

        if (isEmpty()) {
            when {
                username.isNotBlank() -> append(username.trim().first().uppercaseChar())
                userData.email.isNotBlank() -> append(userData.email.trim().first().uppercaseChar())
                else -> append("F")
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient),
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Account Information",
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
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF241C43),
                                    Color(0xFF1C1730)
                                )
                            )
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(accentGradient)
                            .border(2.dp, Color.White.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageBitmap != null) {
                            Image(
                                bitmap = profileImageBitmap,
                                contentDescription = "Profile photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = initials,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = displayFullName,
                        color = textPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = displayUsername,
                        color = softGold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your core profile helps Futuris generate more personal guidance across insights, categories, quiz results, and chat.",
                        color = textSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            1.dp,
                            Color.White.copy(alpha = 0.16f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = textPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Change Profile Photo")
                    }
                }
            }

            FuturisSectionCard(
                cardColor = cardColor,
                borderColor = borderColor
            ) {
                SectionHeader(
                    title = "Editable Profile Details",
                    subtitle = "These are the main personal fields you can update",
                    icon = Icons.Default.Person,
                    titleColor = textPrimary,
                    subtitleColor = textSecondary
                )

                FuturisTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "First Name",
                    icon = Icons.Default.Person,
                    textColor = textPrimary,
                    mutedColor = textSecondary
                )

                FuturisTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Last Name",
                    icon = Icons.Default.Badge,
                    textColor = textPrimary,
                    mutedColor = textSecondary
                )

                FuturisTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    icon = Icons.Outlined.PersonOutline,
                    textColor = textPrimary,
                    mutedColor = textSecondary
                )

                FuturisReadOnlyTextField(
                    value = userData.email,
                    label = "Email Address",
                    icon = Icons.Default.Email,
                    textColor = textPrimary,
                    mutedColor = textSecondary
                )

                Text(
                    text = "Email is locked for now for account security. A dedicated verified email-change flow can be added later.",
                    color = textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            FuturisSectionCard(
                cardColor = cardColor,
                borderColor = borderColor
            ) {
                SectionHeader(
                    title = "Core Prediction Identity",
                    subtitle = "These values shape your Futuris personalization",
                    icon = Icons.Default.CalendarMonth,
                    titleColor = textPrimary,
                    subtitleColor = textSecondary
                )

                ReadOnlyInfoCard(
                    label = "Date of Birth",
                    value = dateOfBirthDisplay,
                    icon = Icons.Default.CalendarMonth,
                    cardColor = fieldColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                ReadOnlyInfoCard(
                    label = "Gender",
                    value = genderDisplay,
                    icon = Icons.Default.Person,
                    cardColor = fieldColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }

            Button(
                onClick = {
                    onSaveClick(
                        AccountInfoUiState(
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            username = username.trim(),
                            email = userData.email.trim(),
                            dateOfBirth = userData.dateOfBirth,
                            gender = userData.gender,
                            notificationsEnabled = userData.notificationsEnabled,
                            insightReminders = userData.insightReminders,
                            profileImageUri = profileImageUri
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = softPurple,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Save Changes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    icon: ImageVector,
    titleColor: Color,
    subtitleColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFD98A),
                modifier = Modifier.size(20.dp)
            )
        }

        Column {
            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                color = subtitleColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = Color.White.copy(alpha = 0.07f)
    )
}

@Composable
fun FuturisSectionCard(
    cardColor: Color,
    borderColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(22.dp)),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = content
        )
    }
}

@Composable
private fun ReadOnlyInfoCard(
    label: String,
    value: String,
    icon: ImageVector,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFD98A),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Text(
                text = label,
                color = textSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                color = textPrimary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FuturisTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    textColor: Color,
    mutedColor: Color,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                color = mutedColor
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = mutedColor
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedContainerColor = Color(0xFF241D3F),
            unfocusedContainerColor = Color(0xFF241D3F),
            cursorColor = Color(0xFFB79CFF),
            focusedIndicatorColor = Color(0xFF9D7BFF),
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.10f),
            focusedLabelColor = Color(0xFFB79CFF),
            unfocusedLabelColor = mutedColor,
            focusedLeadingIconColor = Color(0xFFB79CFF),
            unfocusedLeadingIconColor = mutedColor
        )
    )
}

@Composable
fun FuturisReadOnlyTextField(
    value: String,
    label: String,
    icon: ImageVector,
    textColor: Color,
    mutedColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        label = {
            Text(
                text = label,
                color = mutedColor
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = mutedColor
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedContainerColor = Color(0xFF241D3F),
            unfocusedContainerColor = Color(0xFF241D3F),
            cursorColor = Color.Transparent,
            focusedIndicatorColor = Color.White.copy(alpha = 0.10f),
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.10f),
            focusedLabelColor = mutedColor,
            unfocusedLabelColor = mutedColor,
            focusedLeadingIconColor = mutedColor,
            unfocusedLeadingIconColor = mutedColor
        )
    )
}

private fun loadAccountImageBitmapFromUri(
    context: android.content.Context,
    uri: Uri
): ImageBitmap? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        bitmap?.asImageBitmap()
    } catch (_: Exception) {
        null
    }
}