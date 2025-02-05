package com.example.studentcrm.repository.repository

import com.example.studentcrm.repository.entity.CourseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<CourseEntity, Long> {
    fun existsByCourseName(courseName: String): Boolean
}
