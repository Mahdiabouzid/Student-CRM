package com.example.studentcrm.repository.entity

import com.example.studentcrm.dtos.CourseResponseDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "course")
data class CourseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "COURSE_NAME", nullable = false)
    var courseName: String,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "courses_students",
        joinColumns = [JoinColumn(name = "course_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "student_id", referencedColumnName = "id")],
    )
    var students: MutableList<StudentEntity>? = mutableListOf(),
) {
    fun toCourseResponseDto(): CourseResponseDTO {
        val students = this.students?.map { it.toStudentResponseDto() }
        return CourseResponseDTO(this.id, this.courseName, students?.toMutableList())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CourseEntity

        if (id != other.id) return false
        if (courseName != other.courseName) return false

        return true
    }
}
