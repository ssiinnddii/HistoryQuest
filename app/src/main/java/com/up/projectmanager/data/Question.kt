package com.up.projectmanager.data

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val hint: String
)
