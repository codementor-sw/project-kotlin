package com.github.homework.theme.domain

import com.github.homework.program.domain.Program

import javax.persistence.*

@Entity
@SequenceGenerator(name = "theme_seq_generator", sequenceName = "theme_seq", allocationSize = 10)
class Theme(
    var name: String
) {
    init {
        require(name.isNotBlank()){
            "name is require"
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theme_seq_generator")
    private val id: Long? = null

    @ManyToOne
    @JoinColumn(name = "program_id")
    lateinit var program: Program
}