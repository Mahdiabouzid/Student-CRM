package com.example.studentcrm.repository.entity

import com.example.studentcrm.dtos.CourseStudentResponseDto
import com.example.studentcrm.dtos.StudentResponseDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "student")
data class StudentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "FIRST_NAME", nullable = false)
    var firstName: String,
    @Column(name = "LAST_NAME", nullable = false)
    var lastName: String,
    @Column(name = "EMAIL", nullable = false)
    var email: String,
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @Column(name = "COURSES")
    var courses: MutableList<CourseEntity>? = mutableListOf(),
) {
    fun toStudentResponseDto(): StudentResponseDTO {
        val courses = this.courses?.map { CourseStudentResponseDto(it.id, it.courseName) }
        return StudentResponseDTO(this.id, this.firstName, this.lastName, this.email, courses)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StudentEntity

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false

        return true
    }
}
