package com.example.studentcrm.repository.repository

import com.example.studentcrm.repository.entity.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<StudentEntity, Long> {
    fun existsByEmail(email: String): Boolean
}
