package com.example.studentcrm

import com.example.studentcrm.dtos.CourseRequestDto
import com.example.studentcrm.dtos.StudentRequestDto
import com.example.studentcrm.repository.entity.CourseEntity
import com.example.studentcrm.repository.entity.StudentEntity

object TestDataFactory {
    fun getCourseEntity(
        courseId: Long = 1L,
        courseName: String = "Test Course",
    ) = CourseEntity(courseId, courseName, mutableListOf())

    fun getStudentEntity(
        studentId: Long = 1L,
        firstName: String = "Test",
        email: String = "test@test.com",
    ) = StudentEntity(studentId, firstName, "Test", email, mutableListOf())

    fun getCourseRequest(courseName: String = "Test Course") = CourseRequestDto(courseName)

    fun getStudentRequest(firstName: String = "Test", email: String = "test@test.com") = StudentRequestDto(firstName, "Test", email)
}