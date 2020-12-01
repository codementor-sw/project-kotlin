package com.github.homework.theme.service

import com.github.homework.theme.domain.Theme
import com.github.homework.theme.repository.ThemeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ThemeService {
    fun getOrSaveTheme(themeName: String): Theme
}

@Service
class ThemeServiceImpl(private val themeRepository: ThemeRepository) : ThemeService {
    @Transactional
    override fun getOrSaveTheme(themeName: String): Theme {
        return themeRepository
                .findByName(themeName) ?: themeRepository.save(Theme(themeName))
    }
}
