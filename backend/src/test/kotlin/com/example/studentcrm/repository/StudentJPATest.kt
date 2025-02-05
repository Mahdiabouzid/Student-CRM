package com.example.studentcrm.repository

import com.example.studentcrm.BaseJpaTest
import com.example.studentcrm.repository.repository.StudentRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


internal class StudentJPATest : BaseJpaTest() {
    @Autowired
    private lateinit var cut: StudentRepository

    @Nested
    inner class FindByEmail {
        @Test
        fun `finds by email`() {
            val email = "differenttest@test.com"

            cut.save(data.getStudentEntity(email = email))

            val result = cut.existsByEmail(email)

            result shouldBe true
        }

        @Test
        fun `returns null if no matching email is found`() {
            val result = cut.existsByEmail("other.test@test.com")

            result shouldBe false
        }
    }
}
