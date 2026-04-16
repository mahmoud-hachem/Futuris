package com.example.futuris.data

object ChatMemoryStore {

    private val messages = mutableListOf<String>()

    fun addMessage(message: String) {
        val clean = message.trim()
        if (clean.isNotBlank()) {
            messages.add(clean)
        }
    }

    fun addMessages(newMessages: List<String>) {
        newMessages.forEach { addMessage(it) }
    }

    fun getMessages(): List<String> {
        return messages.toList()
    }

    fun clear() {
        messages.clear()
    }
}