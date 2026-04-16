package com.example.futuris.data

import com.example.futuris.backend.AiInsightResponse
import com.example.futuris.model.QuizAnswer

object OnlineInsightManager {

    suspend fun generateCategoryInsight(
        userId: String,
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        gender: String,
        dateOfBirth: String,
        category: String,
        lifeFocus: String? = null,
        state: String? = null,
        intent: String? = null,
        quizAnswers: List<QuizAnswer> = emptyList(),
        chatMessages: List<String> = emptyList()
    ): AiInsightResponse {

        return HomePredictionManager.generateHomePredictionWithFallback(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            username = username,
            email = email,
            gender = gender,
            dateOfBirth = dateOfBirth,
            category = category,
            lifeFocus = lifeFocus,
            state = state,
            intent = intent,
            quizAnswers = quizAnswers,
            chatMessages = chatMessages
        )
    }
}