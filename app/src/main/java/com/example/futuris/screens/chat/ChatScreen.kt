package com.example.futuris.screens.chat

import androidx.compose.foundation.layout.width
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futuris.R
import com.example.futuris.data.AlertItem
import com.example.futuris.data.AlertMemoryStore
import com.example.futuris.data.ChatMemoryStore
import com.example.futuris.screens.home.BottomTabItem
import com.example.futuris.screens.home.GlassBottomBar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

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
    val context = LocalContext.current

    val prefs = remember {
        context.getSharedPreferences("FuturisPrefs", Context.MODE_PRIVATE)
    }

    var message by remember { mutableStateOf("") }
    var isWaitingForReply by remember { mutableStateOf(false) }

    val hiddenSystemMessages = remember {
        setOf(
            "Error connecting to server",
            "Connecting to Futuris AI...",
            "Futuris AI is taking a little longer than expected. Please try again in a few seconds.",
            "I’m here, but I couldn’t generate a response right now.",
            "I’m here, but I couldn’t generate a response right now. Please try again.",
            "Server error: check API or quota"
        )
    }

    val messages = remember {
        val cleanedStoredMessages = ChatMemoryStore.getStructuredMessages()
            .filterNot { it.text.trim() in hiddenSystemMessages }
            .map {
                ChatMessage(
                    text = it.text,
                    isUser = it.isUser
                )
            }

        mutableStateListOf<ChatMessage>().apply {
            addAll(cleanedStoredMessages)

            if (isEmpty()) {
                val welcomeMessage = "Hello! How can I assist you today?"
                add(ChatMessage(welcomeMessage, false))
                ChatMemoryStore.addMessage(
                    text = welcomeMessage,
                    isUser = false
                )
            }
        }
    }

    val client = remember {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    val tabs = remember {
        listOf(
            BottomTabItem("Home", "home", R.drawable.nav_home),
            BottomTabItem("Chat", "chat", R.drawable.nav_chat),
            BottomTabItem("Alerts", "alerts", R.drawable.nav_alerts),
            BottomTabItem("Profile", "profile", R.drawable.nav_profile)
        )
    }

    fun addAssistantMessage(text: String) {
        messages.add(ChatMessage(text = text, isUser = false))
        ChatMemoryStore.addMessage(
            text = text,
            isUser = false
        )
    }

    fun requestAssistantReply(
        userMessage: String,
        maxRetries: Int = 1
    ) {
        isWaitingForReply = true

        fun makeAttempt(attemptIndex: Int) {
            val json = JSONObject().apply {
                put("message", userMessage)
            }

            val body = json.toString()
                .toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("https://futuris-backend-signup.onrender.com/chat")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Snapshot.withMutableSnapshot {
                        if (attemptIndex < maxRetries) {
                            makeAttempt(attemptIndex + 1)
                        } else {
                            isWaitingForReply = false
                            Toast.makeText(
                                context,
                                "Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseText = response.body?.string().orEmpty()

                    val reply = try {
                        if (!response.isSuccessful) {
                            throw Exception("Server returned ${response.code}")
                        }

                        JSONObject(responseText).optString(
                            "reply",
                            ""
                        ).trim()
                    } catch (e: Exception) {
                        ""
                    }

                    Snapshot.withMutableSnapshot {
                        if (reply.isNotBlank()) {
                            addAssistantMessage(reply)

                            val chatInsightAlertShown =
                                prefs.getBoolean("chat_insight_alert_shown", false)

                            if (!chatInsightAlertShown) {
                                AlertMemoryStore.addAlert(
                                    context = context,
                                    alert = AlertItem(
                                        id = System.currentTimeMillis().toString(),
                                        title = "New Insight from Chat",
                                        message = "Your recent conversation unlocked fresh signals.",
                                        timeLabel = "Now",
                                        category = "chat",
                                        isNew = true
                                    )
                                )

                                prefs.edit()
                                    .putBoolean("chat_insight_alert_shown", true)
                                    .apply()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        isWaitingForReply = false
                    }
                }
            })
        }

        makeAttempt(0)
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
                text = "AI",
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
                if (isWaitingForReply) {
                    item {
                        TypingBubble()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                items(messages.reversed()) { msg ->
                    ChatBubble(msg)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            ChatInputBar(
                value = message,
                isEnabled = !isWaitingForReply,
                onValueChange = { message = it },
                onSendClick = {
                    if (message.isNotBlank() && !isWaitingForReply) {
                        val userMessage = message.trim()

                        messages.add(ChatMessage(text = userMessage, isUser = true))
                        ChatMemoryStore.addMessage(
                            text = userMessage,
                            isUser = true
                        )

                        message = ""
                        requestAssistantReply(userMessage)
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
fun TypingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(0xFF3A2A5F),
                    RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Typing",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                TypingDots()
            }
        }
    }
}

@Composable
fun TypingDots() {
    val transition = rememberInfiniteTransition(label = "typing_dots")

    val dot1 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )

    val dot2 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                delayMillis = 150,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )

    val dot3 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                delayMillis = 300,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Dot(alpha = dot1)
        Spacer(modifier = Modifier.width(4.dp))
        Dot(alpha = dot2)
        Spacer(modifier = Modifier.width(4.dp))
        Dot(alpha = dot3)
    }
}

@Composable
fun Dot(alpha: Float) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .alpha(alpha)
            .background(
                Color(0xFFE7D8FF),
                CircleShape
            )
    )
}

@Composable
fun ChatInputBar(
    value: String,
    isEnabled: Boolean,
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
            onValueChange = {
                if (isEnabled) onValueChange(it)
            },
            singleLine = true,
            enabled = isEnabled,
            textStyle = TextStyle(
                color = if (isEnabled) Color.White else Color(0xFFBFAFD4),
                fontSize = 15.sp
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = if (isEnabled) "Ask me something..." else "Futuris AI is replying...",
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
                        colors = if (isEnabled) {
                            listOf(
                                Color(0xFFB874FF),
                                Color(0xFF8A4DFF)
                            )
                        } else {
                            listOf(
                                Color(0xFF7E6698),
                                Color(0xFF5E4B72)
                            )
                        }
                    )
                )
                .clickable(enabled = isEnabled) { onSendClick() },
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