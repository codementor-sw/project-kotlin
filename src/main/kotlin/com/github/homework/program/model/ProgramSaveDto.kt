package com.github.homework.program.model

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProgramSaveDto(
    val id: Long,
    @field:NotBlank
    val name: String,
    val introduction: String,
    val introductionDetail: String,
    @field:NotBlank
    val region: String,
    @field:NotBlank
    val themeName: String
)