package com.example.futuris.data

import com.example.futuris.backend.AiInsightRequest
import com.example.futuris.backend.AiInsightResponse
import com.example.futuris.backend.QuizAnswerPayload
import com.example.futuris.backend.RetrofitClient
import com.example.futuris.model.QuizAnswer
import com.example.futuris.utils.getZodiacSign

object HomePredictionManager {

    fun generateHomePrediction(
        userId: String,
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        gender: String,
        dateOfBirth: String,
        quizAnswers: List<QuizAnswer> = emptyList(),
        chatMessages: List<String>
    ): String {

        // STEP 1 — Read quiz answers saved in memory store
        val storedQuizAnswers = QuizMemoryStore.getAnswers(userId)

        // STEP 2 — Use stored answers if they exist, otherwise fallback
        val finalQuizAnswers =
            if (storedQuizAnswers.isNotEmpty()) {
                storedQuizAnswers
            } else {
                quizAnswers
            }

        // STEP 3 — Combine Quiz + Chat Scores
        val finalScores =
            PredictionEngine.generateFinalScores(
                finalQuizAnswers,
                chatMessages
            )

        // STEP 4 — Get Top Categories
        val (mainCategory, secondaryCategory) =
            PredictionEngine.getTopCategories(finalScores)

        // STEP 5 — Get Zodiac
        val zodiac = getZodiacSign(dateOfBirth)

        // STEP 6 — Generate Home Insight
        val homeInsight =
            InsightGenerator.generateHomeInsight(
                finalScores,
                zodiac
            )

        return homeInsight
    }

    suspend fun generateHomePredictionWithFallback(
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

        // 1) local fallback prediction
        val localPrediction = generateHomePrediction(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            username = username,
            email = email,
            gender = gender,
            dateOfBirth = dateOfBirth,
            quizAnswers = quizAnswers,
            chatMessages = chatMessages
        )

        return try {
            val storedQuizAnswers = QuizMemoryStore.getAnswers(userId)

            val finalQuizAnswers =
                if (storedQuizAnswers.isNotEmpty()) {
                    storedQuizAnswers
                } else {
                    quizAnswers
                }

            val localScores =
                PredictionEngine.generateFinalScores(
                    finalQuizAnswers,
                    chatMessages
                )

            val zodiac = getZodiacSign(dateOfBirth)

            val topKeywords =
                ChatAnalyzer.extractTopKeywords(chatMessages)

            val request = AiInsightRequest(
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
                zodiac = zodiac,
                quizAnswers = finalQuizAnswers.map {
                    QuizAnswerPayload(
                        questionId = it.questionId,
                        selectedOptionText = it.selectedOptionText
                    )
                },
                chatMessages = chatMessages.takeLast(15),
                localScores = localScores.mapKeys { it.key.name },
                topKeywords = topKeywords,
                localPrediction = localPrediction
            )

            val response =
                RetrofitClient.api.generateAiInsight(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!

                if (body.success && body.insight.isNotBlank()) {
                    body
                } else {
                    AiInsightResponse(
                        success = true,
                        title = "$category Insight",
                        insight = localPrediction,
                        advice = "Trust your current rhythm and keep observing your recent patterns.",
                        energy = "Balanced",
                        focus = category,
                        confidence = 60,
                        source = "local",
                        message = "AI response was empty, local fallback used."
                    )
                }
            } else {
                AiInsightResponse(
                    success = true,
                    title = "$category Insight",
                    insight = localPrediction,
                    advice = "Trust your current rhythm and keep observing your recent patterns.",
                    energy = "Balanced",
                    focus = category,
                    confidence = 60,
                    source = "local",
                    message = "Network call failed, local fallback used."
                )
            }

        } catch (e: Exception) {
            AiInsightResponse(
                success = true,
                title = "$category Insight",
                insight = localPrediction,
                advice = "Trust your current rhythm and keep observing your recent patterns.",
                energy = "Balanced",
                focus = category,
                confidence = 60,
                source = "local",
                message = e.message ?: "Unknown error, local fallback used."
            )
        }
    }
}