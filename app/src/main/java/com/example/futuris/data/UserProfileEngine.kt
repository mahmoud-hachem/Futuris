package com.example.futuris.data

import com.example.futuris.model.*
import com.example.futuris.utils.getZodiacSign

data class UserProfile(

    val userId: String,

    val firstName: String,
    val lastName: String,

    val username: String,
    val email: String,

    val gender: String,

    val dateOfBirth: String,

    val zodiacSign: String,

    val categoryScores: Map<FuturisCategory, Int>,

    val mainCategory: FuturisCategory,

    val secondaryCategory: FuturisCategory

)

object UserProfileEngine {

    fun buildUserProfile(

        userId: String,

        firstName: String,
        lastName: String,

        username: String,
        email: String,

        gender: String,

        dateOfBirth: String,

        answers: List<QuizAnswer>

    ): UserProfile {

        // Calculate Zodiac
        val zodiac = getZodiacSign(dateOfBirth)

        // Calculate Scores
        val scores =
            ScoreEngine.calculateScores(answers)

        // Get main category
        val mainCategory =
            scores.maxByOrNull { it.value }?.key
                ?: FuturisCategory.LIFE_PATH

        // Get secondary category
        val secondaryCategory =
            scores
                .filter { it.key != mainCategory }
                .maxByOrNull { it.value }
                ?.key
                ?: FuturisCategory.LIFE_PATH

        return UserProfile(

            userId = userId,

            firstName = firstName,
            lastName = lastName,

            username = username,
            email = email,

            gender = gender,

            dateOfBirth = dateOfBirth,

            zodiacSign = zodiac,

            categoryScores = scores,

            mainCategory = mainCategory,

            secondaryCategory = secondaryCategory

        )

    }

}