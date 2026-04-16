package com.example.futuris.data

import com.example.futuris.model.*

object PredictionEngine {

    fun generateFinalScores(
        quizAnswers: List<QuizAnswer>,
        chatMessages: List<String>
    ): MutableMap<FuturisCategory, Int> {

        // Step 1 — Base scores from Quiz
        val baseScores =
            ScoreEngine.calculateScores(quizAnswers)

        // Step 2 — Apply Chat impacts
        chatMessages.forEach { message ->

            val chatImpact =
                ChatAnalyzer.analyzeMessage(message)

            chatImpact.forEach { (category, value) ->

                val current =
                    baseScores[category] ?: 0

                baseScores[category] =
                    current + value
            }
        }

        return baseScores
    }

    fun getTopCategories(
        scores: Map<FuturisCategory, Int>
    ): Pair<FuturisCategory, FuturisCategory> {

        val main =
            scores.maxByOrNull { it.value }?.key
                ?: FuturisCategory.LIFE_PATH

        val secondary =
            scores
                .filter { it.key != main }
                .maxByOrNull { it.value }
                ?.key
                ?: FuturisCategory.MOOD

        return Pair(main, secondary)
    }
}