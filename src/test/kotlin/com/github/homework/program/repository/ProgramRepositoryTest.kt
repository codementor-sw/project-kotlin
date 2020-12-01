package com.github.homework.program.repository

import com.github.homework.program.domain.Program
import com.github.homework.theme.domain.Theme
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream

@DataJpaTest(showSql = false)
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class ProgramRepositoryTest(
    private val testEntityManager: TestEntityManager,
    private val programRepository: ProgramRepository
) {
    @Test
    @DisplayName("프로그램이 여러건일때 테스트")
    fun findByPageTest() {
        //given
        IntStream.range(0, 20).forEach { i: Int ->
            val program = Program(
                name="name",
                introduction = "introduction",
                introductionDetail = "introductionDetail",
                region = "region",
                themes = mutableSetOf(Theme("theme$i"))
            )
            testEntityManager.persist(program)
            testEntityManager.flush()
            testEntityManager.clear()
        }

        //when
        val programViewDtos = programRepository.findBy(PageRequest.of(0, 2))
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
}