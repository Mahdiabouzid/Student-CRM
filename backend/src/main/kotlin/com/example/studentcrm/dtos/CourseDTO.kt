package com.example.studentcrm.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CourseResponseDTO(
    val id: Long,
    val courseName: String,
    val students: List<StudentResponseDTO>?,
)

data class CourseStudentResponseDto(
    val id: Long,
    val courseName: String,
)

data class CourseRequestDto(
    @field:NotBlank(message = "Invalid Course name: Empty name")
    @field:Size(max = 35, message = "Course name is too long. Must be of maximum 35 characters")
    val courseName: String = "",
)
