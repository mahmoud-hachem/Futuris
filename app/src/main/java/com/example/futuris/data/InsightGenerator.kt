package com.example.futuris.data

import com.example.futuris.model.FuturisCategory

object InsightGenerator {

    fun generateHomeInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {

        val sortedCategories = scores.entries
            .sortedByDescending { it.value }

        val topCategory = sortedCategories.getOrNull(0)?.key ?: FuturisCategory.LIFE_PATH
        val topScore = sortedCategories.getOrNull(0)?.value ?: 0
        val secondCategory = sortedCategories.getOrNull(1)?.key ?: FuturisCategory.MOOD
        val secondScore = sortedCategories.getOrNull(1)?.value ?: 0

        val zodiacTone = getZodiacTone(zodiac)

        val intensityText = when {
            topScore >= 8 -> "This energy is especially strong right now."
            topScore >= 5 -> "This theme is clearly active in your life."
            else -> "This influence is present, even if quietly."
        }

        return when (topCategory) {

            FuturisCategory.CAREER -> {
                val supportText = when (secondCategory) {
                    FuturisCategory.DECISIONS ->
                        "A key decision may influence your next professional step."
                    FuturisCategory.MOOD ->
                        "Your emotional state may affect how confidently you move forward."
                    FuturisCategory.LIFE_PATH ->
                        "This growth seems connected to your deeper direction and purpose."
                    FuturisCategory.FINANCE ->
                        "Practical results and long-term stability may motivate your effort."
                    FuturisCategory.LOVE ->
                        "Personal relationships may also shape your focus and ambition."
                    else ->
                        "Keep building steadily."
                }

                "Your path is strongly aligned with growth in career and studies. $intensityText $zodiacTone $supportText"
            }

            FuturisCategory.LOVE -> {
                val supportText = when (secondCategory) {
                    FuturisCategory.MOOD ->
                        "Your emotions are closely tied to your relationships right now."
                    FuturisCategory.DECISIONS ->
                        "A personal choice may soon affect a connection or emotional bond."
                    FuturisCategory.LIFE_PATH ->
                        "What you feel in love may also be guiding your bigger direction."
                    FuturisCategory.CAREER ->
                        "Balancing ambition and connection may become important."
                    FuturisCategory.FINANCE ->
                        "Practical concerns could influence emotional harmony."
                    else ->
                        "Pay attention to the signals around closeness and trust."
                }

                "Your emotional world is active right now. $intensityText $zodiacTone $supportText"
            }

            FuturisCategory.FINANCE -> {
                val supportText = when (secondCategory) {
                    FuturisCategory.DECISIONS ->
                        "Smart choices will matter more than quick reactions."
                    FuturisCategory.CAREER ->
                        "Work and financial progress appear closely linked."
                    FuturisCategory.MOOD ->
                        "Stress or calmness may influence how you manage resources."
                    FuturisCategory.LIFE_PATH ->
                        "Money matters may reflect a wider shift in priorities."
                    FuturisCategory.LOVE ->
                        "Financial stability may affect personal comfort and connection."
                    else ->
                        "Stay practical and observant."
                }

                "Financial awareness is rising. $intensityText $zodiacTone $supportText"
            }

            FuturisCategory.MOOD -> {
                val supportText = when (secondCategory) {
                    FuturisCategory.LOVE ->
                        "Relationships may be affecting your emotional balance more than usual."
                    FuturisCategory.DECISIONS ->
                        "Your inner state could shape how clearly you choose your next step."
                    FuturisCategory.LIFE_PATH ->
                        "Your emotions may be pointing toward a deeper personal truth."
                    FuturisCategory.CAREER ->
                        "Pressure linked to work or studies may need better balance."
                    FuturisCategory.FINANCE ->
                        "Practical worries may be draining your energy."
                    else ->
                        "Give yourself space to recover and reflect."
                }

                "Your emotional balance needs attention. $intensityText $zodiacTone $supportText"
            }

            FuturisCategory.DECISIONS -> {
                val supportText = when (secondCategory) {
                    FuturisCategory.LIFE_PATH ->
                        "The choices ahead may shape a bigger chapter in your life."
                    FuturisCategory.MOOD ->
                        "Your emotions may be making this decision feel heavier."
                    FuturisCategory.CAREER ->
                        "A study or career path may require clear commitment."
                    FuturisCategory.LOVE ->
                        "A personal connection may be part of what you are trying to understand."
                    FuturisCategory.FINANCE ->
                        "Practical and financial consequences may need careful thought."
                    else ->
                        "Clarity will be more useful than speed."
                }

                "Important decisions are forming around you. $intensityText $zodiacTone $supportText"
            }

            FuturisCategory.LIFE_PATH -> {
                val supportText = when (secondCategory) {
                    FuturisCategory.DECISIONS ->
                        "A meaningful decision may help define your next direction."
                    FuturisCategory.MOOD ->
                        "Your inner feelings may be guiding you toward needed change."
                    FuturisCategory.CAREER ->
                        "Career and studies may be part of a wider life transition."
                    FuturisCategory.LOVE ->
                        "Relationships may reveal something important about where you are heading."
                    FuturisCategory.FINANCE ->
                        "Your priorities may be shifting toward greater stability and purpose."
                    else ->
                        "Stay open to patterns, lessons, and timing."
                }

                "Your life direction is entering a reflective phase. $intensityText $zodiacTone $supportText"
            }
        }
    }

    fun generateCareerInsight(
        scores: Map<FuturisCategory, Int>,
        zodiac: String
    ): String {

        val careerScore = scores[FuturisCategory.CAREER] ?: 0
        val moodScore = scores[FuturisCategory.MOOD] ?: 0
        val decisionsScore = scores[FuturisCategory.DECISIONS] ?: 0
        val lifePathScore = scores[FuturisCategory.LIFE_PATH] ?: 0

        val zodiacTone = getZodiacTone(zodiac)

        return when {
            careerScore >= 7 && moodScore >= 3 ->
                "A strong period of professional growth is forming around you. $zodiacTone Even if pressure rises, your effort can turn uncertainty into progress."

            careerScore >= 7 && decisionsScore >= 3 ->
                "Your career energy is strong, but an important choice may shape your next step. $zodiacTone Think carefully before committing to a new direction."

            careerScore >= 6 && lifePathScore >= 4 ->
                "Your studies or career are closely tied to your bigger life direction right now. $zodiacTone What you choose next could define a meaningful new chapter."

            careerScore >= 5 ->
                "Career and study progress are active for you now. $zodiacTone Stay disciplined, trust your skills, and keep moving toward long-term success."

            else ->
                "Your career path is still developing quietly. $zodiacTone Keep building consistency, because small efforts now can open larger opportunities later."
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