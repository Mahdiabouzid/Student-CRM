package com.example.studentcrm.controller

import com.example.studentcrm.dtos.CourseRequestDto
import com.example.studentcrm.dtos.CourseResponseDTO
import com.example.studentcrm.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/courses")
class CourseController(private val courseService: CourseService) {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCourse(
        @RequestBody @Valid courseDto: CourseRequestDto,
    ): CourseResponseDTO = courseService.createCourse(courseDto).toCourseResponseDto()

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCourseById(
        @PathVariable("id") id: Long,
    ): CourseResponseDTO = courseService.findCourseById(id).toCourseResponseDto()

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourses(): List<CourseResponseDTO> = courseService.findAllCourses().map { it.toCourseResponseDto() }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(
        @PathVariable("id") id: Long,
    ) {
        courseService.deleteCourse(id)
    }

    @PutMapping("/addStudent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addStudentToCourse(
        @RequestParam("courseId") courseId: Long,
        @RequestParam("studentId") studentId: Long,
    ) {
        courseService.addStudentToCourse(courseId, studentId)
    }

    @PutMapping("/removeStudent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeStudentFromCourse(
        @RequestParam("courseId") courseId: Long,
        @RequestParam("studentId") studentId: Long,
    ) {
        courseService.removeStudentFromCourse(courseId, studentId)
    }

    @PutMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateCourse(
        @PathVariable("id") id: Long,
        @Valid @RequestBody body: CourseRequestDto,
    ) = courseService.updateCourse(id, body)
}
