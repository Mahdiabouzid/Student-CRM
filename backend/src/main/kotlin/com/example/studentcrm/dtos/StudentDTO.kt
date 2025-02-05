package com.example.studentcrm.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class StudentResponseDTO(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val courses: List<CourseStudentResponseDto>?,
)

data class StudentRequestDto(
    @field:NotBlank(message = "Invalid First name: Empty name")
    @field:Size(min = 2, max = 20, message = "Invalid First Name: Must be of 2 - 20 characters")
    val firstName: String = "",

    @field:NotBlank(message = "Invalid Last name: Empty name")
    @field:Size(min = 2, max = 20, message = "Invalid last Name: Must be of 2 - 20 characters")
    val lastName: String = "",

    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "Invalid Email: Empty email")
    val email: String = "",
)
