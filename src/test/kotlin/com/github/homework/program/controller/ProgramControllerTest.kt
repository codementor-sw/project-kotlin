package com.github.homework.program.controller

import com.github.homework.common.BaseControllerTest
import com.github.homework.program.domain.Program
import com.github.homework.program.model.ProgramSaveDto
import com.github.homework.program.repository.ProgramRepository
import com.github.homework.theme.domain.Theme
import com.github.homework.theme.repository.ThemeRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class ProgramControllerTest(
    private val programRepository: ProgramRepository,
    private val themeRepository: ThemeRepository
) : BaseControllerTest() {

    @Throws(Exception::class)
    @DisplayName("프로그램 단건 조회")
    @Test
    fun programTest() {
        val program = givenProgram(mutableSetOf(givenTheme("식도락여행")))
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/programs/{id}", program.id))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("name").value("여수 10미 먹거리"))
            .andExpect(MockMvcResultMatchers.jsonPath("introduction").value("여수시 일대 게장백반, 돌산갓김치등"))
            .andExpect(MockMvcResultMatchers.jsonPath("introductionDetail").value("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"))
            .andExpect(MockMvcResultMatchers.jsonPath("region").value("전라남도 여수시"))
            .andDo(write.document(
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("id").description("id")
                )
            ))
    }

    @Throws(Exception::class)
    @DisplayName("프로그램 단건 조회 결과 없음")
    @Test
    fun programFailTest() {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/programs/{id}", 10L))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Throws(Exception::class)
    @DisplayName("프로그램 page 조회")
    @Test
    fun pageProgramTest() {
        givenProgram(mutableSetOf(givenTheme("식도락여행"), givenTheme("즐거운여행")))
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/programs"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$..id").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$..name").value("여수 10미 먹거리"))
            .andExpect(MockMvcResultMatchers.jsonPath("$..introduction").value("여수시 일대 게장백반, 돌산갓김치등"))
            .andExpect(MockMvcResultMatchers.jsonPath("$..introductionDetail").value("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"))
            .andExpect(MockMvcResultMatchers.jsonPath("$..region").value("전라남도 여수시"))
            .andExpect(MockMvcResultMatchers.jsonPath("totalPages").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("totalElements").value("1"))
    }

    @Test
    @DisplayName("프로그램 저장 정상 케이스")
    @Throws(Exception::class)
    fun saveProgramTest() {
        val programSaveDto = ProgramSaveDto(
            id = 0L,
            name = "여수 10미 먹거리",
            region = "전라남도 여수시",
            themeName = "식도락여행",
            introduction = "여수시 일대 게장백반, 돌산갓김치등",
            introductionDetail =
            "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"
        )
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/programs/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(programSaveDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("success").value("true"))
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("저장 성공"))
            .andDo(write.document(
                requestFields(
                    fieldWithPath("id").description("프로그램 id"),
                    fieldWithPath("name").description("프로그램 이름"),
                    fieldWithPath("introduction").description("소개"),
                    fieldWithPath("introductionDetail").description("상세 소개"),
                    fieldWithPath("region").description("지역 이름"),
                    fieldWithPath("themeName").description("테마 이름")
                )
            ))
    }

    @Test
    @DisplayName("프로그램 저장 name이 없는 케이스")
    @Throws(Exception::class)
    fun saveProgramFailTest() {
        val programSaveDto = ProgramSaveDto(
            id = 0L,
            name = "",
            region = "전라남도 여수시",
            themeName = "식도락여행",
            introduction = "여수시 일대 게장백반, 돌산갓김치등",
            introductionDetail =
            "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"
        )
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/programs/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(programSaveDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("[0].field").value("name"))
            .andExpect(MockMvcResultMatchers.jsonPath("[0].objectName").value("programSaveDto"))
            .andExpect(MockMvcResultMatchers.jsonPath("[0].code").value("NotBlank"))
            .andExpect(MockMvcResultMatchers.jsonPath("[0].defaultMessage").value("must not be blank"))
    }

    @Test
    @DisplayName("프로그램 수정 정상 케이스")
    @Throws(Exception::class)
    fun updateProgramTest() {
        val program = givenProgram(mutableSetOf(givenTheme("식도락여행"), givenTheme("즐거운여행")))
        val programSaveDto = ProgramSaveDto(
            id = program.id!!,
            name = "여수 10미 먹거리",
            region = "전라남도 여수시",
            themeName = "식도락여행",
            introduction = program.introduction,
            introductionDetail = program.introductionDetail
        )

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/programs/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(programSaveDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(write.document(
                requestFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("name").description("프로그램 이름"),
                    fieldWithPath("introduction").description("소개"),
                    fieldWithPath("introductionDetail").description("상세 소개"),
                    fieldWithPath("region").description("지역 이름"),
                    fieldWithPath("themeName").description("테마 이름")
                )))
    }

    @Test
    @DisplayName("프로그램 수정 없는 id로 요청 케이스")
    @Throws(Exception::class)
    fun updateProgramFailTest() {
        val programSaveDto = ProgramSaveDto(
            id = 10L,
            name = "여수 10미 먹거리",
            region = "전라남도 여수시",
            themeName = "식도락여행",
            introduction = "여수시 일대 게장백반, 돌산갓김치등",
            introductionDetail =
            "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"
        )
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/programs/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(programSaveDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    private fun givenProgram(themes: MutableSet<Theme>): Program {
        val program = Program(
            name = "여수 10미 먹거리",
            region = "전라남도 여수시",
            introduction = "여수시 일대 게장백반, 돌산갓김치등",
            introductionDetail =
            "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램",
            themes = themes
        )
        return programRepository.save(program)
    }

    private fun givenTheme(name: String): Theme {
        return themeRepository.save(Theme(name))
    }
}