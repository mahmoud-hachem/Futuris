package com.example.futuris.screens.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.data.ChatMemoryStore
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@Composable
fun ChatScreen(
    firstName: String,
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val safeFirstName = firstName.trim().ifBlank { "User" }

    var message by remember { mutableStateOf("") }

    val messages = remember { mutableStateListOf<ChatMessage>() }

    val client = remember { OkHttpClient() }

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
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x18000000),
                            Color(0x12000000),
                            Color(0x30000000)
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
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Futuris AI",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your personal future assistant",
                color = Color(0xFFE3D6F4),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    ChatBubble(msg)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            ChatInputBar(
                value = message,
                onValueChange = { message = it },
                onSendClick = {
                    if (message.isNotBlank()) {
                        val userMessage = message.trim()

                        messages.add(ChatMessage(userMessage, true))
                        ChatMemoryStore.addMessage(userMessage)

                        message = ""

                        val json = JSONObject()
                        json.put("message", userMessage)

                        val body = json.toString()
                            .toRequestBody("application/json".toMediaTypeOrNull())

                        val request = Request.Builder()
                            .url("https://futuris-backend.onrender.com/chat")
                            .post(body)
                            .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                androidx.compose.runtime.snapshots.Snapshot.withMutableSnapshot {
                                    val errorText = "Error connecting to server"
                                    messages.add(ChatMessage(errorText, false))
                                    ChatMemoryStore.addMessage(errorText)
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val res = response.body?.string().orEmpty()

                                val reply = try {
                                    JSONObject(res).getString("reply")
                                } catch (e: Exception) {
                                    "Server error: check API or quota"
                                }

                                androidx.compose.runtime.snapshots.Snapshot.withMutableSnapshot {
                                    messages.add(ChatMessage(reply, false))
                                    ChatMemoryStore.addMessage(reply)
                                }
                            }
                        })
                    }
                }
            )

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
fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isUser) Color(0xFF9C27B0) else Color(0xFF3A2A5F),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xC71D0730),
                        Color(0xCC2B0B45)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Color(0x55FFFFFF)
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 15.sp
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "Ask Futuris something...",
                        color = Color(0xFFBFAFD4),
                        fontSize = 15.sp
                    )
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFB874FF),
                            Color(0xFF8A4DFF)
                        )
                    )
                )
                .clickable { onSendClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "➜",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}