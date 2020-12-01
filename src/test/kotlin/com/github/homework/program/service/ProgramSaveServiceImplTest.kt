package com.github.homework.program.service

import com.github.homework.program.domain.Program
import com.github.homework.program.domain.updateProgram
import com.github.homework.program.exception.ProgramNotFoundException
import com.github.homework.program.model.ProgramSaveDto
import com.github.homework.program.repository.ProgramRepository
import com.github.homework.theme.domain.Theme
import com.github.homework.theme.service.ThemeService
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.*
import org.mockito.BDDMockito.eq
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProgramSaveServiceImplTest {
    @Mock
    private lateinit var themeService: ThemeService

    @Mock
    private lateinit var programRepository: ProgramRepository

    @InjectMocks
    private lateinit var programSaveService: ProgramSaveServiceImpl

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<Program>

    @Test
    @DisplayName("프로그램 저장")
    fun saveProgramTest() {
        //given
        val programSaveDto: ProgramSaveDto = givenProgramSaveDto(null)
        given(themeService.getOrSaveTheme(programSaveDto.themeName)).willReturn(Theme("식도락여행"))
        //when
        programSaveService.saveProgram(programSaveDto)
        //then
        Mockito.verify(programRepository, Mockito.times(1)).save(argumentCaptor.capture())
        then(argumentCaptor.value.name).isEqualTo("여수 10미 먹거리")
        then(argumentCaptor.value.region).isEqualTo("전라남도 여수시")
        then(argumentCaptor.value.introduction).isEqualTo("여수시 일대 게장백반, 돌산갓김치등")
        then(argumentCaptor.value.introductionDetail).isEqualTo(
            "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
        then(argumentCaptor.value.themes.iterator().next().name).isEqualTo("식도락여행")
    }

    @Test
    @DisplayName("프로그램 수정")
    @Disabled("mockito 문제로 일단 비활성화")
    fun updateProgramTest() {
        //given
        val programSaveDto: ProgramSaveDto = givenProgramSaveDto(1L)
        val mockProgram: Program = Mockito.mock(Program::class.java)
        given<Optional<Program>>(programRepository.findById(1L)).willReturn(Optional.of(mockProgram))
        val givenTheme = Theme("식도락여행")
        given(themeService.getOrSaveTheme(programSaveDto.themeName)).willReturn(givenTheme)

        //when
        programSaveService.updateProgram(programSaveDto)

        //then
        Mockito.verify(mockProgram, Mockito.times(1)).updateProgram(
            ArgumentMatchers.eq("여수 10미 먹거리"),
            ArgumentMatchers.eq("여수시 일대 게장백반, 돌산갓김치등"),
            ArgumentMatchers.eq("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"),
            ArgumentMatchers.eq("전라남도 여수시"),
            eq(setOf(givenTheme)))
    }

    @Test
    @DisplayName("존재 하지 않는 프로그램 수정")
    fun updateNotFoundProgramTest() {
        //given
        val programSaveDto: ProgramSaveDto = givenProgramSaveDto(null)

        //when
        //then
        Assertions.assertThrows(ProgramNotFoundException::class.java) { programSaveService.updateProgram(programSaveDto) }
    }

    private fun givenProgramSaveDto(id: Long?) = ProgramSaveDto(
        id = id ?: 0L,
        name = "여수 10미 먹거리",
        region = "전라남도 여수시",
        themeName = "식도락여행",
        introduction = "여수시 일대 게장백반, 돌산갓김치등",
        introductionDetail =
        "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"
    )

}