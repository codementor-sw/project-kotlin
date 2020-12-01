package com.github.homework.theme.repository

import com.github.homework.theme.domain.Theme
import com.github.homework.theme.repository.ThemeRepository
import org.assertj.core.api.BDDAssertions
import org.assertj.core.api.BDDAssertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.transaction.annotation.Transactional

@DataJpaTest(showSql = false)
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ThemeRepositoryTest(
   private val themeRepository: ThemeRepository,
   private val testEntityManager: TestEntityManager
) {

    @Test
    @DisplayName("저장된 테마 이름 검색")
    fun saveFindByNameTest() {
        //given
        testEntityManager.persist(Theme("test"))
        //when
        val optionalTheme = themeRepository.findByName("test")
        //then
        then(optionalTheme).isNotNull
    }

    @Test
    @DisplayName("저장되지않은 테마 이름 검색")
    fun notSaveFindByNameTest() {
        //given
        //when
        val optionalTheme = this.themeRepository.findByName("test")
        //then
        then(optionalTheme).isNull()
    }
}