package com.example.studentcrm.systemTest

import com.example.studentcrm.dtos.CourseRequestDto
import com.example.studentcrm.dtos.StudentRequestDto
import com.example.studentcrm.repository.repository.CourseRepository
import com.example.studentcrm.repository.repository.StudentRepository
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpHeaders.CONTENT_TYPE
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentCrmSystemTest
    @Autowired
    constructor(
        private val studentRepository: StudentRepository,
        private val courseRepository: CourseRepository,
    ) {
        @BeforeEach
        fun setup(
            @LocalServerPort port: Int,
        ) {
            RestAssured.port = port
        }

        @Test
        fun `system test`() {
            val studentId =
                Given {
                    body(StudentRequestDto(firstName = "Dummy", lastName = "Test", email = "uniquetest@test.com"))
                    header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                } When {
                    post("/student-crm/api/students/create")
                } Then {
                    statusCode(HttpStatus.CREATED.value())
                } Extract {
                    jsonPath().getLong("id")
                }

            studentRepository.existsByEmail("uniquetest@test.com") shouldBe true

            val courseId =
                Given {
                    body(CourseRequestDto(courseName = "Test Course"))
                    header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                } When {
                    post("/student-crm/api/courses/create")
                } Then {
                    statusCode(HttpStatus.CREATED.value())
                } Extract {
                    jsonPath().getLong("id")
                }

            courseRepository.existsByCourseName("Test Course") shouldBe true

            Given {
                body(CourseRequestDto(courseName = "Test Course"))
                header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            } When {
                post("/student-crm/api/courses/create")
            } Then {
                statusCode(HttpStatus.CONFLICT.value())
            }

            When {
                get("/student-crm/api/courses/$courseId")
            } Then {
                statusCode(HttpStatus.OK.value())
            }

            When {
                get("/student-crm/api/students/$studentId")
            } Then {
                statusCode(HttpStatus.OK.value())
            }

            When {
                put("/student-crm/api/courses/addStudent?courseId=$courseId&studentId=$studentId")
            } Then {
                statusCode(HttpStatus.NO_CONTENT.value())
            }

            When {
                put("/student-crm/api/courses/removeStudent?courseId=$courseId&studentId=$studentId")
            } Then {
                statusCode(HttpStatus.NO_CONTENT.value())
            }

            When {
                delete("/student-crm/api/students/delete/$studentId")
            } Then {
                statusCode(HttpStatus.NO_CONTENT.value())
            }

            studentRepository.findByIdOrNull(studentId) shouldBe null

            When {
                delete("/student-crm/api/courses/delete/$courseId")
            } Then {
                statusCode(HttpStatus.NO_CONTENT.value())
            }

            courseRepository.findByIdOrNull(courseId) shouldBe null
        }
    }
