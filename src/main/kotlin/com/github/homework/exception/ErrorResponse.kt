package com.github.homework.exception

data class ErrorResponse(
    val field: String,
    val objectName: String,
    val code: String?,
    val defaultMessage: String?,
    val rejectedValue: String?
)