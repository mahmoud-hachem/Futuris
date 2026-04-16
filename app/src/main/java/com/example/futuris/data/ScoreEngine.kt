package com.example.futuris.data

import com.example.futuris.model.FuturisCategory
import com.example.futuris.model.QuizAnswer

object ScoreEngine {

    fun calculateScores(
        answers: List<QuizAnswer>
    ): MutableMap<FuturisCategory, Int> {

        val scores = mutableMapOf(
            FuturisCategory.LOVE to 0,
            FuturisCategory.CAREER to 0,
            FuturisCategory.FINANCE to 0,
            FuturisCategory.MOOD to 0,
            FuturisCategory.DECISIONS to 0,
            FuturisCategory.LIFE_PATH to 0
        )

        answers.forEach { answer ->

            val question = QuizRepository.mandatoryQuestions
                .find { it.id == answer.questionId }

            val selectedOption = question
                ?.options
                ?.find { it.text == answer.selectedOptionText }

            selectedOption
                ?.traitImpact
                ?.forEach { (category, value) ->

                    val currentValue = scores[category] ?: 0
                    scores[category] = currentValue + value
                }
        }

        return scores
    }
}