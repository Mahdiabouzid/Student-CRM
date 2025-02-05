package com.example.studentcrm.exceptions

import com.example.studentcrm.dtos.ErrorMessage
import java.util.stream.Collectors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    /**
     * handle MethodArgumentNotValidException thrown when encountering an invalid request
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<Any?> {
        val errors =
            ex.bindingResult.fieldErrors
                .stream().map { obj: FieldError -> obj.defaultMessage }.collect(Collectors.toList())
        return ResponseEntity<Any?>(getErrorsMap(errors), HttpStatus.BAD_REQUEST)
    }

    private fun getErrorsMap(errors: List<String?>): Map<String, List<String?>> {
        val errorResponse: MutableMap<String, List<String?>> = HashMap()
        errorResponse["errors"] = errors
        return errorResponse
    }

    @ExceptionHandler
    fun handleCourseNotFoundException(ex: CourseNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(ex.message, HttpStatus.NOT_FOUND.value())
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleCourseExistsException(ex: CourseExistsException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(ex.message, HttpStatus.CONFLICT.value())
        return ResponseEntity(errorMessage, HttpStatus.CONFLICT)
    }

    @ExceptionHandler
    fun handleStudentNotFoundException(ex: StudentNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(ex.message, HttpStatus.NOT_FOUND.value())
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleEmailInUseException(ex: EmailInUseException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(ex.message, HttpStatus.CONFLICT.value())
        return ResponseEntity(errorMessage, HttpStatus.CONFLICT)
    }

    @ExceptionHandler
    fun handleStudentAlreadyInCourseException(ex: StudentAlreadyInCourseException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(ex.message, HttpStatus.CONFLICT.value())
        return ResponseEntity(errorMessage, HttpStatus.CONFLICT)
    }

    @ExceptionHandler
    fun handleStudentNotInCourseException(ex: StudentNotInCourseException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(ex.message, HttpStatus.BAD_REQUEST.value())
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}
