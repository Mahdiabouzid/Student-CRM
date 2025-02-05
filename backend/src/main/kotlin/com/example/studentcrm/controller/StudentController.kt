package com.example.studentcrm.controller

import com.example.studentcrm.dtos.StudentRequestDto
import com.example.studentcrm.dtos.StudentResponseDTO
import com.example.studentcrm.service.StudentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/students")
class StudentController(private val studentService: StudentService) {
    /**
     * create new Student
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(
        @RequestBody @Valid studentDTO: StudentRequestDto,
    ): StudentResponseDTO = studentService.createStudent(studentDTO).toStudentResponseDto()

    /**
     * return single student
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleStudent(
        @PathVariable("id") id: Long,
    ): StudentResponseDTO = studentService.findById(id).toStudentResponseDto()

    /**
     * return all students
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllStudents(): List<StudentResponseDTO> = studentService.findAll().map { it.toStudentResponseDto() }

    /**
     * delete student
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(
        @PathVariable("id") id: Long,
    ): Unit = studentService.deleteStudent(id)

    /**
     * update student
     */
    @PutMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateStudentData(
        @PathVariable("id") id: Long,
        @Valid @RequestBody body: StudentRequestDto,
    ) = studentService.updateStudent(id, body)
}
