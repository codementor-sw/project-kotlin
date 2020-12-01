package com.github.homework.program.service

import com.github.homework.program.domain.Program
import com.github.homework.program.domain.updateProgram
import com.github.homework.program.exception.ProgramNotFoundException
import com.github.homework.program.model.ProgramSaveDto
import com.github.homework.program.repository.ProgramRepository
import com.github.homework.theme.service.ThemeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ProgramSaveService {
    fun saveProgram(programSaveDto: ProgramSaveDto)

    @Throws(ProgramNotFoundException::class)
    fun updateProgram(programSaveDto: ProgramSaveDto)
}

@Service
class ProgramSaveServiceImpl(
    val themeService: ThemeService,
    val programRepository: ProgramRepository
) : ProgramSaveService {

    @Transactional
    override fun saveProgram(programSaveDto: ProgramSaveDto) {
        val theme = themeService.getOrSaveTheme(programSaveDto.themeName)
        val program = Program(name = programSaveDto.name,
            introduction = programSaveDto.introduction,
            introductionDetail = programSaveDto.introductionDetail,
            region = programSaveDto.region,
            themes = mutableSetOf(theme)
        )
        programRepository.save(program)
    }

    @Transactional
    @Throws(ProgramNotFoundException::class)
    override fun updateProgram(programSaveDto: ProgramSaveDto) {
        val program = programRepository.findById(programSaveDto.id).orElseThrow { ProgramNotFoundException() }
        val theme = themeService.getOrSaveTheme(programSaveDto.themeName)
        program.updateProgram(programSaveDto.name,
                programSaveDto.introduction,
                programSaveDto.introductionDetail,
                programSaveDto.region,
                setOf(theme)
        )
    }
}