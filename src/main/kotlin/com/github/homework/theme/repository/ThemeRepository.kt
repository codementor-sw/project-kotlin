package com.github.homework.theme.repository

import com.github.homework.theme.domain.Theme
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ThemeRepository : JpaRepository<Theme, Long> {
    fun findByName(name: String): Theme?
}