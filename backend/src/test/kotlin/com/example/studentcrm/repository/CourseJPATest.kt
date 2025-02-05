package com.example.studentcrm.repository

import com.example.studentcrm.BaseJpaTest
import com.example.studentcrm.repository.repository.CourseRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class CourseJPATest : BaseJpaTest() {
    @Autowired
    private lateinit var cut: CourseRepository

    @Nested
    inner class FindByCourseName {
        @Test
        fun `finds course by name`() {
            cut.save(data.getCourseEntity())

            val result = cut.existsByCourseName(data.getCourseEntity().courseName)

            result shouldBe true
        }

        @Test
        fun `returns null if name not found`() {
            val result = cut.existsByCourseName("Test")

            result shouldBe false
        }
    }
}
