package com.example.futuris.model

data class QuizQuestion(

    val id: String,

    val text: String,

    val category: FuturisCategory,

    val traitType: TraitType,

    val options: List<QuizOption>,

    val isMandatory: Boolean = false

)

data class QuizOption(

    val text: String,

    // This map controls how answering affects prediction categories
    val traitImpact: Map<FuturisCategory, Int>

)

enum class TraitType {

    FOCUS,            // main life focus
    EMOTION,          // emotional state
    CONCERN,          // main worry
    DECISION_STYLE,   // how user makes choices
    GOAL              // desired outcome

}