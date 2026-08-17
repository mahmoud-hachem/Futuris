package com.example.futuris.data

import android.content.Context

object OnboardingStateManager {

    private const val PREFS_NAME = "FuturisOnboardingPrefs"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun quizCompletedKey(userId: String): String {
        return "quiz_completed_$userId"
    }

    private fun firstInsightGeneratedKey(userId: String): String {
        return "first_insight_generated_$userId"
    }

    private fun onboardingFinishedKey(userId: String): String {
        return "onboarding_finished_$userId"
    }

    fun hasCompletedQuiz(
        context: Context,
        userId: String
    ): Boolean {
        if (userId.isBlank()) return false
        return prefs(context).getBoolean(quizCompletedKey(userId), false)
    }

    fun setQuizCompleted(
        context: Context,
        userId: String,
        completed: Boolean
    ) {
        if (userId.isBlank()) return
        prefs(context)
            .edit()
            .putBoolean(quizCompletedKey(userId), completed)
            .apply()
    }

    fun hasGeneratedFirstInsight(
        context: Context,
        userId: String
    ): Boolean {
        if (userId.isBlank()) return false
        return prefs(context).getBoolean(firstInsightGeneratedKey(userId), false)
    }

    fun setFirstInsightGenerated(
        context: Context,
        userId: String,
        generated: Boolean
    ) {
        if (userId.isBlank()) return
        prefs(context)
            .edit()
            .putBoolean(firstInsightGeneratedKey(userId), generated)
            .apply()
    }

    fun isOnboardingFinished(
        context: Context,
        userId: String
    ): Boolean {
        if (userId.isBlank()) return false
        return prefs(context).getBoolean(onboardingFinishedKey(userId), false)
    }

    fun setOnboardingFinished(
        context: Context,
        userId: String,
        finished: Boolean
    ) {
        if (userId.isBlank()) return
        prefs(context)
            .edit()
            .putBoolean(onboardingFinishedKey(userId), finished)
            .apply()
    }

    fun completeOnboarding(
        context: Context,
        userId: String
    ) {
        if (userId.isBlank()) return

        prefs(context)
            .edit()
            .putBoolean(quizCompletedKey(userId), true)
            .putBoolean(firstInsightGeneratedKey(userId), true)
            .putBoolean(onboardingFinishedKey(userId), true)
            .apply()
    }

    fun resetOnboarding(
        context: Context,
        userId: String
    ) {
        if (userId.isBlank()) return

        prefs(context)
            .edit()
            .remove(quizCompletedKey(userId))
            .remove(firstInsightGeneratedKey(userId))
            .remove(onboardingFinishedKey(userId))
            .apply()
    }
}