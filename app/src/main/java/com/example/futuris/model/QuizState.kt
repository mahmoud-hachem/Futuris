package com.example.futuris.model

data class QuizState(

    // Stores all answers given by the user
    val answers: MutableList<QuizAnswer> = mutableListOf(),

    // Tracks which question we are currently showing
    var currentQuestionIndex: Int = 0,

    // Marks if quiz is finished
    var completed: Boolean = false

)