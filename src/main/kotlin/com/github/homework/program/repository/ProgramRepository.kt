package com.github.homework.program.repository

import com.github.homework.program.domain.Program
import com.github.homework.program.domain.QProgram
import com.github.homework.program.model.ProgramViewDto
import com.github.homework.theme.domain.QTheme
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.support.PageableExecutionUtils
import java.util.*
import java.util.stream.Collectors

interface ProgramRepository : JpaRepository<Program, Long>, ProgramCustomRepository

interface ProgramCustomRepository {
    fun findBy(pageable: Pageable): Page<ProgramViewDto>
}

class ProgramCustomRepositoryImpl : QuerydslRepositorySupport(Program::class.java), ProgramCustomRepository {
    override fun findBy(pageable: Pageable): Page<ProgramViewDto> {
        val query: JPQLQuery<Program> = querydsl!!
                .applyPagination(pageable, from(QProgram.program)
                        .join(QProgram.program.themes, QTheme.theme).fetchJoin()
                        .distinct()
                )
        val collect = query.fetch().stream()
                .map { p: Program ->
                    ProgramViewDto(
                            p.id!!,
                            p.name,
                            p.introduction,
                            p.introductionDetail,
                            p.region
                    )
                }.collect(Collectors.toList())
        return PageableExecutionUtils.getPage(collect, pageable) { query.fetchCount() }
    }
}