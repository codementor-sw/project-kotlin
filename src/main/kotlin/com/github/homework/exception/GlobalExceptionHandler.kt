package com.github.homework.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * 정의되지 않은 예외는 INTERNAL_SERVER_ERROR 처리한다
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        log.error("handleException", e)
        return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }


    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다. HttpMessageConverter 에서 등록한 HttpMessageConverter
     * binding 못할경우 발생 주로 @RequestBody, @RequestParam 어노테이션에서 발생
     */
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleMethodArgumentNotValidException(
            e: MethodArgumentNotValidException): ResponseEntity<List<ErrorResponse>> {
        log.error("handleMethodArgumentNotValidException", e)
        val errorResponseList = e.bindingResult.fieldErrors.map {
            ErrorResponse(field = it.field,
                    objectName = it.objectName,
                    code = it.code,
                    defaultMessage = it.defaultMessage,
                    rejectedValue = it.rejectedValue?.toString())
        }.toList()
        return ResponseEntity(errorResponseList, HttpStatus.BAD_REQUEST)
    }
}