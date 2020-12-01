package com.github.homework.program.domain

import com.github.homework.theme.domain.Theme
import java.util.function.Consumer
import javax.persistence.*

@Entity
@SequenceGenerator(name = "program_seq_generator", sequenceName = "program_seq", allocationSize = 10)
class Program(
    var name: String,
    var introduction: String,
    var introductionDetail: String,
    var region: String,
    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL])
    var themes: MutableSet<Theme> = mutableSetOf()
) {
    init {
        themes.forEach{ theme: Theme -> theme.program = this }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_seq_generator")
    var id: Long? = null
}

fun Program.updateProgram(
    name: String,
    introduction: String,
    introductionDetail: String,
    region: String,
    themes: Set<Theme>
) {
    this.name = name
    this.introduction = introduction
    this.introductionDetail = introductionDetail
    this.region = region
    this.themes.retainAll(themes)
    themes.forEach { theme: Theme -> theme.program = this }
}