package com.github.homework.program.controller

import com.github.homework.program.exception.ProgramNotFoundException
import com.github.homework.program.model.ProgramSaveDto
import com.github.homework.program.model.ProgramViewDto
import com.github.homework.program.model.SimpleResponse
import com.github.homework.program.service.ProgramSaveService
import com.github.homework.program.service.ProgramViewService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/programs"])
class ProgramController(
    private val programViewService: ProgramViewService,
    private val programSaveService: ProgramSaveService
) {

    @GetMapping
    fun pageBy(
            @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable): ResponseEntity<Page<ProgramViewDto>> {
        return ResponseEntity.ok(programViewService.pageBy(pageable))
    }

    @GetMapping("/{id}")
    fun getBy(@PathVariable id: Long): ResponseEntity<ProgramViewDto> {
        val programViewDto = programViewService.getBy(id)
        return programViewDto.map { body: ProgramViewDto -> ResponseEntity.ok(body) }.orElseGet { ResponseEntity.notFound().build() }
    }

    @PostMapping
    fun saveProgram(@RequestBody @Valid programSaveDto: ProgramSaveDto): ResponseEntity<SimpleResponse> {
        programSaveService.saveProgram(programSaveDto)
        return ResponseEntity.ok(SimpleResponse(true, "저장 성공"))
    }

    @PutMapping
    fun updateProgram(@RequestBody programSaveDto: @Valid ProgramSaveDto): ResponseEntity<SimpleResponse> {
        try {
            programSaveService.updateProgram(programSaveDto)
        } catch (e: ProgramNotFoundException) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(SimpleResponse(true, "수정 성공"))
    }
}