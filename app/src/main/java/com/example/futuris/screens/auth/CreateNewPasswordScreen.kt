package com.example.futuris.screens.auth

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.backend.ChangePasswordRequest
import com.example.futuris.backend.RetrofitClient
import com.example.futuris.components.FuturisBackground
import com.example.futuris.components.FuturisButton
import com.example.futuris.components.FuturisPasswordField
import com.example.futuris.ui.theme.SoftText
import com.example.futuris.ui.theme.TitleWhite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreateNewPasswordScreen(
    currentEmail: String,
    onResetDone: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    FuturisBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            Image(
                painter = painterResource(id = R.drawable.futuris_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(110.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Change password",
                color = TitleWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Update your password without leaving the app",
                color = SoftText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(42.dp))

            FuturisPasswordField(
                value = currentPassword,
                onValueChange = { if (!isLoading) currentPassword = it },
                placeholder = "Current password",
                passwordVisible = showCurrentPassword,
                onTogglePassword = {
                    if (!isLoading) showCurrentPassword = !showCurrentPassword
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            FuturisPasswordField(
                value = newPassword,
                onValueChange = { if (!isLoading) newPassword = it },
                placeholder = "New password",
                passwordVisible = showNewPassword,
                onTogglePassword = {
                    if (!isLoading) showNewPassword = !showNewPassword
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            FuturisPasswordField(
                value = confirmPassword,
                onValueChange = { if (!isLoading) confirmPassword = it },
                placeholder = "Confirm new password",
                passwordVisible = showConfirmPassword,
                onTogglePassword = {
                    if (!isLoading) showConfirmPassword = !showConfirmPassword
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FuturisButton(
                text = if (isLoading) "Updating..." else "Update password",
                onClick = {
                    if (isLoading) return@FuturisButton

                    focusManager.clearFocus()

                    val cleanCurrent = currentPassword.trim()
                    val cleanNew = newPassword.trim()
                    val cleanConfirm = confirmPassword.trim()

                    if (cleanCurrent.isBlank() || cleanNew.isBlank() || cleanConfirm.isBlank()) {
                        Toast.makeText(
                            context,
                            "Please fill all password fields",
                            Toast.LENGTH_LONG
                        ).show()
                        return@FuturisButton
                    }

                    if (cleanNew.length < 6) {
                        Toast.makeText(
                            context,
                            "New password must be at least 6 characters",
                            Toast.LENGTH_LONG
                        ).show()
                        return@FuturisButton
                    }

                    if (cleanNew != cleanConfirm) {
                        Toast.makeText(
                            context,
                            "New passwords do not match",
                            Toast.LENGTH_LONG
                        ).show()
                        return@FuturisButton
                    }

                    if (currentEmail.isBlank()) {
                        Toast.makeText(
                            context,
                            "Missing account email",
                            Toast.LENGTH_LONG
                        ).show()
                        return@FuturisButton
                    }

                    isLoading = true

                    scope.launch(Dispatchers.IO) {
                        try {
                            val response = RetrofitClient.api.changePassword(
                                ChangePasswordRequest(
                                    email = currentEmail.trim().lowercase(),
                                    currentPassword = cleanCurrent,
                                    newPassword = cleanNew
                                )
                            )

                            Handler(Looper.getMainLooper()).post {
                                isLoading = false

                                if (response.isSuccessful && response.body() != null) {
                                    Toast.makeText(
                                        context,
                                        response.body()?.message ?: "Password changed successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    onResetDone()
                                } else {
                                    val errorText = response.errorBody()?.string().orEmpty()

                                    Toast.makeText(
                                        context,
                                        if (errorText.isNotBlank()) errorText else "Failed to change password",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            Handler(Looper.getMainLooper()).post {
                                isLoading = false

                                Toast.makeText(
                                    context,
                                    "Network error: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            FuturisButton(
                text = "Back",
                onClick = {
                    if (!isLoading) onBackClick()
                }
            )
        }
    }
}