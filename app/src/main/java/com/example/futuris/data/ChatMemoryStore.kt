package com.example.futuris.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class StoredChatMessage(
    val text: String,
    val isUser: Boolean
)

object ChatMemoryStore {

    private val messages = mutableStateListOf<StoredChatMessage>()

    // OLD SUPPORT
    // keeps older files working
    fun addMessage(message: String) {
        val clean = message.trim()
        if (clean.isNotBlank()) {
            messages.add(
                StoredChatMessage(
                    text = clean,
                    isUser = false
                )
            )
        }
    }

    // NEW SUPPORT
    // use this in ChatScreen
    fun addMessage(text: String, isUser: Boolean) {
        val clean = text.trim()
        if (clean.isNotBlank()) {
            messages.add(
                StoredChatMessage(
                    text = clean,
                    isUser = isUser
                )
            )
        }
    }

    fun addMessages(newMessages: List<String>) {
        newMessages.forEach { addMessage(it) }
    }

    // OLD SUPPORT
    // returns only text so old category files still compile
    fun getMessages(): List<String> {
        return messages.map { it.text }
    }

    // NEW SUPPORT
    // returns full structured messages for chat UI
    fun getStructuredMessages(): SnapshotStateList<StoredChatMessage> {
        return messages
    }

    fun clear() {
        messages.clear()
    }
}