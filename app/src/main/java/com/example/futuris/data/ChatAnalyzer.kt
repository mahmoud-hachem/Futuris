package com.example.futuris.data

import com.example.futuris.model.FuturisCategory

object ChatAnalyzer {

    fun analyzeMessage(
        message: String
    ): Map<FuturisCategory, Int> {

        val lowerMessage = message.lowercase()

        val impact = mutableMapOf(
            FuturisCategory.LOVE to 0,
            FuturisCategory.CAREER to 0,
            FuturisCategory.FINANCE to 0,
            FuturisCategory.MOOD to 0,
            FuturisCategory.DECISIONS to 0,
            FuturisCategory.LIFE_PATH to 0
        )

        // ================= LOVE =================
        if (
            lowerMessage.contains("love") ||
            lowerMessage.contains("relationship") ||
            lowerMessage.contains("partner") ||
            lowerMessage.contains("breakup") ||
            lowerMessage.contains("girlfriend") ||
            lowerMessage.contains("boyfriend") ||
            lowerMessage.contains("crush") ||
            lowerMessage.contains("date") ||
            lowerMessage.contains("dating") ||
            lowerMessage.contains("romantic") ||
            lowerMessage.contains("feelings") ||
            lowerMessage.contains("jealous") ||
            lowerMessage.contains("miss him") ||
            lowerMessage.contains("miss her")
        ) {
            impact[FuturisCategory.LOVE] =
                impact[FuturisCategory.LOVE]!! + 2
        }

        // ================= CAREER =================
        if (
            lowerMessage.contains("career") ||
            lowerMessage.contains("job") ||
            lowerMessage.contains("study") ||
            lowerMessage.contains("exam") ||
            lowerMessage.contains("work") ||
            lowerMessage.contains("university") ||
            lowerMessage.contains("school") ||
            lowerMessage.contains("grades") ||
            lowerMessage.contains("interview") ||
            lowerMessage.contains("promotion") ||
            lowerMessage.contains("boss") ||
            lowerMessage.contains("project")
        ) {
            impact[FuturisCategory.CAREER] =
                impact[FuturisCategory.CAREER]!! + 2
        }

        // ================= FINANCE =================
        if (
            lowerMessage.contains("money") ||
            lowerMessage.contains("finance") ||
            lowerMessage.contains("debt") ||
            lowerMessage.contains("salary") ||
            lowerMessage.contains("cash") ||
            lowerMessage.contains("payment") ||
            lowerMessage.contains("expensive") ||
            lowerMessage.contains("save") ||
            lowerMessage.contains("saving") ||
            lowerMessage.contains("budget") ||
            lowerMessage.contains("buy") ||
            lowerMessage.contains("income")
        ) {
            impact[FuturisCategory.FINANCE] =
                impact[FuturisCategory.FINANCE]!! + 2
        }

        // ================= MOOD =================
        if (
            lowerMessage.contains("sad") ||
            lowerMessage.contains("stress") ||
            lowerMessage.contains("stressed") ||
            lowerMessage.contains("tired") ||
            lowerMessage.contains("anxious") ||
            lowerMessage.contains("anxiety") ||
            lowerMessage.contains("happy") ||
            lowerMessage.contains("lonely") ||
            lowerMessage.contains("depressed") ||
            lowerMessage.contains("overthinking") ||
            lowerMessage.contains("confused") ||
            lowerMessage.contains("lost") ||
            lowerMessage.contains("exhausted")
        ) {
            impact[FuturisCategory.MOOD] =
                impact[FuturisCategory.MOOD]!! + 2
        }

        // ================= DECISIONS =================
        if (
            lowerMessage.contains("decision") ||
            lowerMessage.contains("choose") ||
            lowerMessage.contains("option") ||
            lowerMessage.contains("should i") ||
            lowerMessage.contains("which one") ||
            lowerMessage.contains("what should i do") ||
            lowerMessage.contains("i don't know") ||
            lowerMessage.contains("i dont know") ||
            lowerMessage.contains("conflicted") ||
            lowerMessage.contains("between two")
        ) {
            impact[FuturisCategory.DECISIONS] =
                impact[FuturisCategory.DECISIONS]!! + 2
        }

        // ================= LIFE PATH =================
        if (
            lowerMessage.contains("future") ||
            lowerMessage.contains("purpose") ||
            lowerMessage.contains("path") ||
            lowerMessage.contains("destiny") ||
            lowerMessage.contains("meaning") ||
            lowerMessage.contains("direction") ||
            lowerMessage.contains("who am i") ||
            lowerMessage.contains("become") ||
            lowerMessage.contains("next step") ||
            lowerMessage.contains("my life")
        ) {
            impact[FuturisCategory.LIFE_PATH] =
                impact[FuturisCategory.LIFE_PATH]!! + 2
        }

        return impact
    }

    fun analyzeMessages(
        messages: List<String>
    ): Map<FuturisCategory, Int> {

        val totalImpact = mutableMapOf(
            FuturisCategory.LOVE to 0,
            FuturisCategory.CAREER to 0,
            FuturisCategory.FINANCE to 0,
            FuturisCategory.MOOD to 0,
            FuturisCategory.DECISIONS to 0,
            FuturisCategory.LIFE_PATH to 0
        )

        messages.forEach { message ->
            val messageImpact = analyzeMessage(message)

            messageImpact.forEach { (category, score) ->
                totalImpact[category] =
                    totalImpact[category]!! + score
            }
        }

        return totalImpact
    }

    fun extractTopKeywords(
        messages: List<String>
    ): List<String> {

        val keywordPool = listOf(
            "love", "relationship", "partner", "girlfriend", "boyfriend",
            "date", "dating", "feelings", "career", "job", "study", "exam",
            "work", "money", "finance", "salary", "budget", "sad", "stress",
            "tired", "anxious", "happy", "decision", "choose", "future",
            "purpose", "path", "destiny"
        )

        val foundKeywords = mutableListOf<String>()

        val fullText = messages.joinToString(" ").lowercase()

        keywordPool.forEach { keyword ->
            if (fullText.contains(keyword)) {
                foundKeywords.add(keyword)
            }
        }

        return foundKeywords.take(10)
    }
}