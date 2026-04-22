package com.example.futuris.data

import android.util.Log
import com.example.futuris.backend.AiInsightRequest
import com.example.futuris.backend.AiInsightResponse
import com.example.futuris.backend.QuizAnswerPayload
import com.example.futuris.backend.RetrofitClient
import com.example.futuris.model.QuizAnswer
import com.example.futuris.utils.getZodiacSign

object HomePredictionManager {

    private const val TAG = "FuturisInsightDebug"

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

        Log.d(TAG, "generateHomePrediction() started")

        val storedQuizAnswers = QuizMemoryStore.getAnswers(userId)

        val finalQuizAnswers =
            if (storedQuizAnswers.isNotEmpty()) {
                storedQuizAnswers
            } else {
                quizAnswers
            }

        Log.d(TAG, "generateHomePrediction() finalQuizAnswers size = ${finalQuizAnswers.size}")
        Log.d(TAG, "generateHomePrediction() chatMessages size = ${chatMessages.size}")

        val finalScores =
            PredictionEngine.generateFinalScores(
                finalQuizAnswers,
                chatMessages
            )

        Log.d(TAG, "generateHomePrediction() finalScores = $finalScores")

        val zodiac = getZodiacSign(dateOfBirth)

        Log.d(TAG, "generateHomePrediction() zodiac = $zodiac")

        val result = InsightGenerator.generateHomeInsight(
            finalScores,
            zodiac
        )

        Log.d(TAG, "generateHomePrediction() result = $result")

        return result
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

        Log.d(TAG, "==============================")
        Log.d(TAG, "generateHomePredictionWithFallback() START")
        Log.d(TAG, "userId = $userId")
        Log.d(TAG, "firstName = $firstName")
        Log.d(TAG, "category = $category")
        Log.d(TAG, "dateOfBirth = $dateOfBirth")
        Log.d(TAG, "quizAnswers incoming size = ${quizAnswers.size}")
        Log.d(TAG, "chatMessages incoming size = ${chatMessages.size}")

        return try {
            Log.d(TAG, "STEP 1: generating localPrediction")

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

            Log.d(TAG, "STEP 1 OK: localPrediction = $localPrediction")

            Log.d(TAG, "STEP 2: reading stored quiz answers")

            val storedQuizAnswers = QuizMemoryStore.getAnswers(userId)

            val finalQuizAnswers =
                if (storedQuizAnswers.isNotEmpty()) {
                    storedQuizAnswers
                } else {
                    quizAnswers
                }

            Log.d(TAG, "STEP 2 OK: finalQuizAnswers size = ${finalQuizAnswers.size}")

            Log.d(TAG, "STEP 3: generating localScores")

            val localScores =
                PredictionEngine.generateFinalScores(
                    finalQuizAnswers,
                    chatMessages
                )

            Log.d(TAG, "STEP 3 OK: localScores = $localScores")

            Log.d(TAG, "STEP 4: zodiac")
            val zodiac = getZodiacSign(dateOfBirth)
            Log.d(TAG, "STEP 4 OK: zodiac = $zodiac")

            Log.d(TAG, "STEP 5: top keywords")
            val topKeywords =
                ChatAnalyzer.extractTopKeywords(chatMessages)
            Log.d(TAG, "STEP 5 OK: topKeywords = $topKeywords")

            Log.d(TAG, "STEP 6: building request")

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

            Log.d(TAG, "STEP 6 OK: request built")
            Log.d(TAG, "Request category = ${request.category}")
            Log.d(TAG, "Request zodiac = ${request.zodiac}")
            Log.d(TAG, "Request quizAnswers size = ${request.quizAnswers.size}")
            Log.d(TAG, "Request chatMessages size = ${request.chatMessages.size}")
            Log.d(TAG, "Request localScores = ${request.localScores}")
            Log.d(TAG, "Request topKeywords = ${request.topKeywords}")

            Log.d(TAG, "STEP 7: calling Retrofit api.generateAiInsight()")

            val response = RetrofitClient.api.generateAiInsight(request)

            Log.d(TAG, "STEP 7 OK: network response received")
            Log.d(TAG, "HTTP code = ${response.code()}")
            Log.d(TAG, "HTTP success = ${response.isSuccessful}")

            val body = response.body()
            Log.d(TAG, "Response body = $body")

            if (response.isSuccessful && body != null) {
                if (body.success && body.insight.isNotBlank()) {
                    Log.d(TAG, "FINAL OK: returning online insight")
                    body
                } else {
                    Log.e(TAG, "FINAL FAIL: body exists but success=false or insight blank")
                    AiInsightResponse(
                        success = false,
                        title = "$category Insight",
                        insight = "",
                        questions = emptyList(),
                        advice = "",
                        energy = "",
                        focus = category,
                        confidence = 0,
                        source = "online-error",
                        message = body.message.ifBlank {
                            "Online insight returned an empty response."
                        }
                    )
                }
            } else {
                val errorText = try {
                    response.errorBody()?.string().orEmpty()
                } catch (e: Exception) {
                    ""
                }

                Log.e(TAG, "FINAL FAIL: response not successful")
                Log.e(TAG, "Error body = $errorText")

                AiInsightResponse(
                    success = false,
                    title = "$category Insight",
                    insight = "",
                    questions = emptyList(),
                    advice = "",
                    energy = "",
                    focus = category,
                    confidence = 0,
                    source = "network-error",
                    message = "Network call failed: ${response.code()} $errorText"
                )
            }

        } catch (e: Exception) {
            Log.e(TAG, "EXCEPTION in generateHomePredictionWithFallback()", e)

            AiInsightResponse(
                success = false,
                title = "$category Insight",
                insight = "",
                questions = emptyList(),
                advice = "",
                energy = "",
                focus = category,
                confidence = 0,
                source = "exception",
                message = e.message ?: "Unknown error while generating online insight."
            )
        }
    }
}