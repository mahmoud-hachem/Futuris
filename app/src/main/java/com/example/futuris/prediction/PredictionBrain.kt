package com.example.futuris.prediction

import com.example.futuris.data.InsightGenerator
import com.example.futuris.data.PredictionEngine
import com.example.futuris.model.FuturisCategory
import com.example.futuris.model.QuizAnswer
import com.example.futuris.utils.getZodiacSign

object PredictionBrain {

    fun generateInsight(
        category: String,
        firstName: String = "",
        dateOfBirth: String = "",
        quizAnswers: List<QuizAnswer> = emptyList(),
        chatMessages: List<String> = emptyList()
    ): String {

        val finalScores = PredictionEngine.generateFinalScores(
            quizAnswers = quizAnswers,
            chatMessages = chatMessages
        )

        val zodiac = getZodiacSign(dateOfBirth)

        val rawInsight = when (category.lowercase()) {
            "career" -> {
                generateCareerInsight(
                    scores = finalScores,
                    zodiac = zodiac
                )
            }

            "love" -> {
                generateLoveInsight(
                    scores = finalScores,
                    zodiac = zodiac
                )
            }

            "finance" -> {
                generateFinanceInsight(
                    scores = finalScores,
                    zodiac = zodiac
                )
            }

            "mood" -> {
                generateMoodInsight(
                    scores = finalScores,
                    zodiac = zodiac
                )
            }

            "decisions" -> {
                generateDecisionsInsight(
                    scores = finalScores,
                    zodiac = zodiac
                )
            }

            "lifepath" -> {
                generateLifePathInsight(
                    scores = finalScores,
                    zodiac = zodiac
                )
            }

            "home" -> {
                InsightGenerator.generateHomeInsight(
                    scores = finalScores,
                    zodiac = zodiac
                )
            }

            else -> {
                "The signals around you are still forming. Stay observant, because your next pattern is not fully revealed yet."
            }
        }

        return if (firstName.isNotBlank()) {
            "$firstName, $rawInsight"
        } else {
            rawInsight
        }
    }

    private fun generateCareerInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {
        val careerScore = scores[FuturisCategory.CAREER] ?: 0
        val decisionsScore = scores[FuturisCategory.DECISIONS] ?: 0
        val moodScore = scores[FuturisCategory.MOOD] ?: 0
        val lifePathScore = scores[FuturisCategory.LIFE_PATH] ?: 0
        val zodiacTone = getZodiacTone(zodiac)

        return when {
            careerScore >= 8 && decisionsScore >= 4 ->
                "Your career energy is entering a decisive phase. $zodiacTone A serious opportunity or commitment may soon ask for confidence, structure, and clear direction."

            careerScore >= 8 && moodScore >= 4 ->
                "You have strong potential for growth in work or studies, but pressure may be rising internally. $zodiacTone Protect your focus, because emotional balance will affect performance."

            careerScore >= 7 && lifePathScore >= 4 ->
                "Your ambitions are no longer only practical; they are becoming part of your bigger path. $zodiacTone The effort you make now could shape an important chapter of your future."

            careerScore >= 5 ->
                "Progress in career and studies is building steadily around you. $zodiacTone Stay disciplined and consistent, because the results may arrive through persistence more than speed."

            else ->
                "Your professional path is developing quietly for now. $zodiacTone This is a good time to strengthen skills, improve routine, and prepare for a stronger opening ahead."
        }
    }

    private fun generateLoveInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {
        val loveScore = scores[FuturisCategory.LOVE] ?: 0
        val moodScore = scores[FuturisCategory.MOOD] ?: 0
        val decisionsScore = scores[FuturisCategory.DECISIONS] ?: 0
        val lifePathScore = scores[FuturisCategory.LIFE_PATH] ?: 0
        val zodiacTone = getZodiacTone(zodiac)

        return when {
            loveScore >= 8 && moodScore >= 4 ->
                "Your emotional world is very active right now. $zodiacTone Feelings may intensify quickly, and honesty will matter more than appearances in your relationships."

            loveScore >= 8 && decisionsScore >= 4 ->
                "Love energy is strong, but a choice may soon define where a connection is going. $zodiacTone Clarity in what you truly want can change everything."

            loveScore >= 7 && lifePathScore >= 4 ->
                "Your heart seems tied to a deeper lesson or direction in your life. $zodiacTone A relationship or emotional truth may reveal something important about who you are becoming."

            loveScore >= 5 ->
                "Affection, closeness, and emotional openness are growing around you. $zodiacTone Let trust build naturally, because meaningful bonds often deepen through simple and real moments."

            else ->
                "Love is moving more quietly for now. $zodiacTone Stay open without forcing anything, because the right emotional rhythm often appears when pressure fades."
        }
    }

    private fun generateFinanceInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {
        val financeScore = scores[FuturisCategory.FINANCE] ?: 0
        val decisionsScore = scores[FuturisCategory.DECISIONS] ?: 0
        val moodScore = scores[FuturisCategory.MOOD] ?: 0
        val careerScore = scores[FuturisCategory.CAREER] ?: 0
        val zodiacTone = getZodiacTone(zodiac)

        return when {
            financeScore >= 8 && decisionsScore >= 4 ->
                "Financial matters are becoming more serious and strategic. $zodiacTone A practical decision made now could strengthen your stability in the near future."

            financeScore >= 8 && moodScore >= 4 ->
                "Money concerns may be affecting your peace of mind more than usual. $zodiacTone Calm planning and self-control will help you regain a sense of security."

            financeScore >= 7 && careerScore >= 4 ->
                "Your financial progress appears connected to your work, effort, or future plans. $zodiacTone Building consistency now may create stronger long-term results."

            financeScore >= 5 ->
                "Your awareness around resources and stability is rising. $zodiacTone Smart choices, even small ones, can gradually create a more secure foundation."

            else ->
                "Your financial path is still strengthening step by step. $zodiacTone This is a useful phase for patience, budgeting, and avoiding choices made under pressure."
        }
    }

    private fun generateMoodInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {
        val moodScore = scores[FuturisCategory.MOOD] ?: 0
        val lifePathScore = scores[FuturisCategory.LIFE_PATH] ?: 0
        val loveScore = scores[FuturisCategory.LOVE] ?: 0
        val financeScore = scores[FuturisCategory.FINANCE] ?: 0
        val zodiacTone = getZodiacTone(zodiac)

        return when {
            moodScore >= 8 && lifePathScore >= 4 ->
                "Your emotions are deeply linked to your sense of meaning right now. $zodiacTone What you are feeling may be guiding you toward an inner truth that should not be ignored."

            moodScore >= 8 && loveScore >= 4 ->
                "Your emotional energy is strong, and relationships may be influencing your balance more than usual. $zodiacTone Give yourself room to breathe before reacting too quickly."

            moodScore >= 7 && financeScore >= 4 ->
                "Practical worries may be draining your energy behind the scenes. $zodiacTone Rest, order, and emotional honesty can help you feel steadier again."

            moodScore >= 5 ->
                "Your inner state needs care and attention. $zodiacTone Small habits of rest, reflection, and emotional balance can restore a calmer rhythm."

            else ->
                "Your mood is moving through a quieter phase. $zodiacTone Keep supporting your energy gently, because stability often grows through small daily choices."
        }
    }

    private fun generateDecisionsInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {
        val decisionsScore = scores[FuturisCategory.DECISIONS] ?: 0
        val lifePathScore = scores[FuturisCategory.LIFE_PATH] ?: 0
        val careerScore = scores[FuturisCategory.CAREER] ?: 0
        val loveScore = scores[FuturisCategory.LOVE] ?: 0
        val zodiacTone = getZodiacTone(zodiac)

        return when {
            decisionsScore >= 8 && lifePathScore >= 4 ->
                "A meaningful decision is approaching, and it may influence your larger direction in life. $zodiacTone Reflection will serve you better than rushing for fast certainty."

            decisionsScore >= 8 && careerScore >= 4 ->
                "A choice linked to studies, work, or future structure may soon require commitment. $zodiacTone Preparation will give you more power than panic."

            decisionsScore >= 7 && loveScore >= 4 ->
                "A personal or emotional situation may soon ask you for honesty and clear boundaries. $zodiacTone The right choice may begin with admitting what you already feel."

            decisionsScore >= 5 ->
                "Clarity is slowly forming around an important matter. $zodiacTone Stay patient, because the strongest answers often arrive when the noise settles."

            else ->
                "Your decision path is still unfolding gradually. $zodiacTone Keep observing patterns and timing, because insight sometimes arrives before certainty."
        }
    }

    private fun generateLifePathInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {
        val lifePathScore = scores[FuturisCategory.LIFE_PATH] ?: 0
        val moodScore = scores[FuturisCategory.MOOD] ?: 0
        val careerScore = scores[FuturisCategory.CAREER] ?: 0
        val decisionsScore = scores[FuturisCategory.DECISIONS] ?: 0
        val zodiacTone = getZodiacTone(zodiac)

        return when {
            lifePathScore >= 8 && moodScore >= 4 ->
                "Your future direction feels emotionally significant right now. $zodiacTone What keeps returning to your heart may be pointing toward the next chapter of your life."

            lifePathScore >= 8 && careerScore >= 4 ->
                "Your larger purpose seems to be connecting with ambition, growth, and long-term effort. $zodiacTone A practical action now may support a future that feels more aligned."

            lifePathScore >= 7 && decisionsScore >= 4 ->
                "You are nearing a phase where one key choice could shape your direction more clearly. $zodiacTone Trust the signs that repeat instead of chasing random noise."

            lifePathScore >= 5 ->
                "You are moving through a reflective stage of becoming. $zodiacTone Pay attention to repeated lessons, because they may reveal where life is guiding you."

            else ->
                "Your life path is still revealing itself slowly. $zodiacTone Keep moving with intention, because meaning often becomes clear while you are already in motion."
        }
    }

    private fun getZodiacTone(zodiac: String): String {
        return when (zodiac) {
            "Aries" -> "Your bold nature pushes you forward."
            "Taurus" -> "Your patience gives strength to your progress."
            "Gemini" -> "Your curiosity opens unexpected doors."
            "Cancer" -> "Your emotional awareness guides your choices."
            "Leo" -> "Your confidence fuels your ambitions."
            "Virgo" -> "Your attention to detail strengthens your path."
            "Libra" -> "Balance and fairness shape your outcomes."
            "Scorpio" -> "Your intensity sharpens your focus."
            "Sagittarius" -> "Your adventurous spirit leads to discovery."
            "Capricorn" -> "Your discipline builds lasting success."
            "Aquarius" -> "Your originality brings fresh perspectives."
            "Pisces" -> "Your intuition helps reveal hidden paths."
            else -> "Your journey continues with quiet strength."
        }
    }
}