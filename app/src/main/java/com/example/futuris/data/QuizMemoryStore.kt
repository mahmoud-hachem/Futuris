package com.example.futuris.data

import android.content.Context
import android.content.SharedPreferences
import com.example.futuris.model.QuizAnswer
import org.json.JSONArray
import org.json.JSONObject

object QuizMemoryStore {

    private const val PREFS_NAME = "FuturisQuizMemory"
    private const val KEY_PREFIX = "quiz_answers_"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
        }
    }

    fun saveAnswer(
        userId: String,
        questionId: String,
        selectedOptionText: String
    ) {
        val safePrefs = prefs ?: return

        val currentAnswers = getAnswers(userId).toMutableList()

        val existingIndex = currentAnswers.indexOfFirst { it.questionId == questionId }

        val newAnswer = QuizAnswer(
            questionId = questionId,
            selectedOptionText = selectedOptionText
        )

        if (existingIndex != -1) {
            currentAnswers[existingIndex] = newAnswer
        } else {
            currentAnswers.add(newAnswer)
        }

        saveAnswersList(
            safePrefs = safePrefs,
            userId = userId,
            answers = currentAnswers
        )
    }

    fun getAnswers(userId: String): List<QuizAnswer> {
        val safePrefs = prefs ?: return emptyList()

        val jsonString = safePrefs.getString(makeUserKey(userId), null) ?: return emptyList()

        return try {
            val jsonArray = JSONArray(jsonString)
            val answers = mutableListOf<QuizAnswer>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)

                answers.add(
                    QuizAnswer(
                        questionId = item.optString("questionId", ""),
                        selectedOptionText = item.optString("selectedOptionText", "")
                    )
                )
            }

            answers
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getAnswerForQuestion(
        userId: String,
        questionId: String
    ): QuizAnswer? {
        return getAnswers(userId).find { it.questionId == questionId }
    }

    fun clearAnswers(userId: String) {
        val safePrefs = prefs ?: return

        safePrefs.edit()
            .remove(makeUserKey(userId))
            .apply()
    }

    private fun saveAnswersList(
        safePrefs: SharedPreferences,
        userId: String,
        answers: List<QuizAnswer>
    ) {
        val jsonArray = JSONArray()

        answers.forEach { answer ->
            val jsonObject = JSONObject().apply {
                put("questionId", answer.questionId)
                put("selectedOptionText", answer.selectedOptionText)
            }
            jsonArray.put(jsonObject)
        }

        safePrefs.edit()
            .putString(makeUserKey(userId), jsonArray.toString())
            .apply()
    }

    private fun makeUserKey(userId: String): String {
        return KEY_PREFIX + userId
    }
}