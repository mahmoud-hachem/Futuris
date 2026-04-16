package com.example.futuris.data

import com.example.futuris.model.*

object QuizRepository {

    val mandatoryQuestions: List<QuizQuestion> = listOf(

        // QUESTION 1 — MAIN FOCUS

        QuizQuestion(
            id = "focus_main",

            text = "Which area of your life feels most important to you right now?",

            category = FuturisCategory.LIFE_PATH,

            traitType = TraitType.FOCUS,

            isMandatory = true,

            options = listOf(

                QuizOption(
                    text = "Love & Relationships",
                    traitImpact = mapOf(
                        FuturisCategory.LOVE to 4
                    )
                ),

                QuizOption(
                    text = "Career & Studies",
                    traitImpact = mapOf(
                        FuturisCategory.CAREER to 4
                    )
                ),

                QuizOption(
                    text = "Finance & Money",
                    traitImpact = mapOf(
                        FuturisCategory.FINANCE to 4
                    )
                ),

                QuizOption(
                    text = "Mood & Emotional Balance",
                    traitImpact = mapOf(
                        FuturisCategory.MOOD to 4
                    )
                ),

                QuizOption(
                    text = "Decisions & Guidance",
                    traitImpact = mapOf(
                        FuturisCategory.DECISIONS to 4
                    )
                ),

                QuizOption(
                    text = "Life Direction & Purpose",
                    traitImpact = mapOf(
                        FuturisCategory.LIFE_PATH to 4
                    )
                )

            )
        ),

        // QUESTION 2 — EMOTIONAL STATE

        QuizQuestion(
            id = "emotion_state",

            text = "How have you been feeling recently about this area?",

            category = FuturisCategory.MOOD,

            traitType = TraitType.EMOTION,

            isMandatory = true,

            options = listOf(

                QuizOption(
                    "Calm and stable",
                    mapOf(
                        FuturisCategory.MOOD to 1
                    )
                ),

                QuizOption(
                    "Motivated and focused",
                    mapOf(
                        FuturisCategory.CAREER to 2
                    )
                ),

                QuizOption(
                    "Stressed or overwhelmed",
                    mapOf(
                        FuturisCategory.MOOD to 3
                    )
                ),

                QuizOption(
                    "Confused or uncertain",
                    mapOf(
                        FuturisCategory.DECISIONS to 3
                    )
                ),

                QuizOption(
                    "Drained or tired",
                    mapOf(
                        FuturisCategory.MOOD to 2
                    )
                ),

                QuizOption(
                    "Hopeful but unsure",
                    mapOf(
                        FuturisCategory.LIFE_PATH to 2
                    )
                )

            )
        ),

        // QUESTION 3 — MAIN CONCERN

        QuizQuestion(
            id = "main_concern",

            text = "What concerns you the most right now?",

            category = FuturisCategory.DECISIONS,

            traitType = TraitType.CONCERN,

            isMandatory = true,

            options = listOf(

                QuizOption(
                    "Making the right decision",
                    mapOf(
                        FuturisCategory.DECISIONS to 3
                    )
                ),

                QuizOption(
                    "Fear of failure",
                    mapOf(
                        FuturisCategory.CAREER to 2,
                        FuturisCategory.MOOD to 2
                    )
                ),

                QuizOption(
                    "Uncertainty about the future",
                    mapOf(
                        FuturisCategory.LIFE_PATH to 3
                    )
                ),

                QuizOption(
                    "Emotional stress",
                    mapOf(
                        FuturisCategory.MOOD to 3
                    )
                ),

                QuizOption(
                    "Financial pressure",
                    mapOf(
                        FuturisCategory.FINANCE to 3
                    )
                )

            )
        ),

        // QUESTION 4 — DECISION STYLE

        QuizQuestion(
            id = "decision_style",

            text = "When facing important choices, how do you usually decide?",

            category = FuturisCategory.DECISIONS,

            traitType = TraitType.DECISION_STYLE,

            isMandatory = true,

            options = listOf(

                QuizOption(
                    "I rely on logic and planning",
                    mapOf(
                        FuturisCategory.DECISIONS to 2
                    )
                ),

                QuizOption(
                    "I follow my emotions",
                    mapOf(
                        FuturisCategory.MOOD to 2
                    )
                ),

                QuizOption(
                    "I trust my intuition",
                    mapOf(
                        FuturisCategory.LIFE_PATH to 2
                    )
                ),

                QuizOption(
                    "I ask others for advice",
                    mapOf(
                        FuturisCategory.LOVE to 1
                    )
                ),

                QuizOption(
                    "I often feel uncertain",
                    mapOf(
                        FuturisCategory.DECISIONS to 3
                    )
                )

            )
        ),

        // QUESTION 5 — CURRENT GOAL

        QuizQuestion(
            id = "current_goal",

            text = "What outcome would make you feel satisfied right now?",

            category = FuturisCategory.LIFE_PATH,

            traitType = TraitType.GOAL,

            isMandatory = true,

            options = listOf(

                QuizOption(
                    "Achieve personal happiness",
                    mapOf(
                        FuturisCategory.LOVE to 2,
                        FuturisCategory.MOOD to 2
                    )
                ),

                QuizOption(
                    "Succeed in my work or studies",
                    mapOf(
                        FuturisCategory.CAREER to 3
                    )
                ),

                QuizOption(
                    "Improve financial stability",
                    mapOf(
                        FuturisCategory.FINANCE to 3
                    )
                ),

                QuizOption(
                    "Find clarity and direction",
                    mapOf(
                        FuturisCategory.LIFE_PATH to 3
                    )
                ),

                QuizOption(
                    "Make confident decisions",
                    mapOf(
                        FuturisCategory.DECISIONS to 3
                    )
                )

            )
        )

    )

}