package com.github.homework.program.service

import com.github.homework.program.domain.Program
import com.github.homework.program.model.ProgramViewDto
import com.github.homework.program.repository.ProgramRepository
import com.github.homework.theme.domain.Theme
import org.assertj.core.api.BDDAssertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.collections.listOf

@ExtendWith(MockitoExtension::class)
internal class ProgramViewServiceImplTest {
    @Mock
    private lateinit var programRepository: ProgramRepository

    @InjectMocks
    private lateinit var programViewService: ProgramViewServiceImpl

    @Test
    @DisplayName("프로그램이 여러개 일때")
    fun pageByTest() {
        //given
        val programViewDto = ProgramViewDto(1L, "name", "introduction", "introductionDetail", "region")
        given(programRepository.findBy(PageRequest.of(0, 100)))
            .willReturn(
                PageImpl(listOf(programViewDto, programViewDto))
            )

        //when
        val programViewDtos = programViewService.pageBy(PageRequest.of(0, 100))
        //then
        then(programViewDtos.content).hasSize(2)
        then(programViewDtos.content).allSatisfy { (id, name, introduction, introductionDetail, region) ->
            then(id).isGreaterThan(0L)
            then(name).isEqualTo("name")
            then(introduction).isEqualTo("introduction")
            then(introductionDetail).isEqualTo("introductionDetail")
            then(region).isEqualTo("region")
        }
    }

    @DisplayName("프로그램이 한개 일때")
    @Test
    fun byTest() {
        //given
        val program = Program(
            name = "name",
            introduction = "introduction",
            introductionDetail = "introductionDetail",
            region = "region",
            themes = mutableSetOf(Theme("theme"),Theme("theme1"))
        ).apply {
            id = 1L
        }
        given(programRepository.findById(1L)).willReturn(Optional.of(program))
        //when
        val optionalProgramViewDto = programViewService.getBy(1L)
        //then
        then(optionalProgramViewDto).hasValueSatisfying { (_, name, introduction, introductionDetail, region) ->
            then(name).isEqualTo("name")
            then(introduction).isEqualTo("introduction")
            then(introductionDetail).isEqualTo("introductionDetail")
            then(region).isEqualTo("region")

        }
    }
}