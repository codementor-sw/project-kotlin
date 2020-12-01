package com.github.homework.program.model

data class ProgramViewDto(
    val id: Long,
    val name: String,
    val introduction: String,
    val introductionDetail: String,
    val region: String
)