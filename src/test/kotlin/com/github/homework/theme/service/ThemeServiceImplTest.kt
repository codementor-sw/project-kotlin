package com.github.homework.theme.service

import com.github.homework.theme.domain.Theme
import com.github.homework.theme.repository.ThemeRepository
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.isA
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ThemeServiceImplTest {
    @Mock
    private lateinit var themeRepository: ThemeRepository

    @InjectMocks
    private lateinit var  themeService: ThemeServiceImpl

    @Test
    @DisplayName("테마이름이 없는경우")
    fun emptyThemeNameTest() {

        //given
        //when
        val error = Assertions.assertThrows(IllegalArgumentException::class.java) {
            themeService.getOrSaveTheme("")
        }

        //then
        Assertions.assertEquals("name is require", error.message)
    }

    @Test
    @DisplayName("테마이름이 한개인 경우 저장")
    fun oneThemeNameTest() {
        //given
        given(themeRepository.save(isA(Theme::class.java))).willReturn(Theme("자연체험"))

        //when
        val theme = themeService.getOrSaveTheme("자연체험")

        //then
        then(theme.name).isEqualTo("자연체험")
    }
}