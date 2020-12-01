package com.github.homework.program.service

import com.github.homework.program.domain.Program
import com.github.homework.program.model.ProgramViewDto
import com.github.homework.program.repository.ProgramRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ProgramViewService {
    fun getBy(id: Long): Optional<ProgramViewDto>
    fun pageBy(pageable: Pageable): Page<ProgramViewDto>
}

@Service
@Transactional(readOnly = true)
class ProgramViewServiceImpl(private val programRepository: ProgramRepository) : ProgramViewService {
    override fun getBy(id: Long): Optional<ProgramViewDto> {
        val byId = programRepository.findById(id)
        return byId.map { p: Program ->
            ProgramViewDto(
                    p.id!!,
                    p.name,
                    p.introduction,
                    p.introductionDetail,
                    p.region
            )
        }
    }

    override fun pageBy(pageable: Pageable): Page<ProgramViewDto> {
        return programRepository.findBy(pageable)
    }
}