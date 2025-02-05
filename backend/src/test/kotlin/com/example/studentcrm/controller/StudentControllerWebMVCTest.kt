package com.example.studentcrm.controller

import com.example.studentcrm.BaseWebMvcTest
import com.example.studentcrm.service.StudentService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(StudentController::class)
internal class StudentControllerWebMVCTest : BaseWebMvcTest() {
    @MockkBean
    private lateinit var studentService: StudentService

    private val defaultStudentPath = "/api/students"

        @Nested
        inner class GetStudentById {
            @Test
            fun `should return existing student and a response status 200`() {
                every { studentService.findById(1L) } returns data.getStudentEntity()

                mvc.get("$defaultStudentPath/1")
                    .andExpect {
                        status { isOk() }
                        content {
                            json(mapper.writeValueAsString(data.getStudentEntity()))
                        }
                    }
                verify(exactly = 1) { studentService.findById(1L) }
            }
        }

        @Nested
        inner class CreateStudent {
            @Test
            fun `should return 201, checks the createStudent method is called exactly once and the newly created student is returned`() {
                every { studentService.createStudent(data.getStudentRequest()) } returns data.getStudentEntity()

                mvc.post("$defaultStudentPath/create") {
                    content = mapper.writeValueAsString(data.getStudentRequest())
                    contentType = MediaType.APPLICATION_JSON
                }
                    .andExpect {
                        status { isCreated() }
                        content {
                            json(
                               mapper.writeValueAsString(data.getStudentEntity())
                            )
                        }
                    }
                verify(exactly = 1) { studentService.createStudent(data.getStudentRequest()) }
            }

            @Test
            fun `should return error invalid email empty email`() {

                mvc.post("$defaultStudentPath/create") {
                    contentType = MediaType.APPLICATION_JSON
                    content = mapper.writeValueAsString(data.getStudentRequest(email = ""))
                }.andExpect {
                    status { isBadRequest() }
                    content {
                        json(
                            """ 
                            {
                              "errors": ["Invalid Email: Empty email"]
                            }    
                            """.trimIndent(),
                            strict = true,
                        )
                    }
                }
            }
        }

        @Nested
        inner class DeleteStudent {
            @Test
            fun `should return 209 and checks that deleteStudent method is called exactly once`() {
                every { studentService.deleteStudent(1L) } just Runs

                mvc.delete("$defaultStudentPath/delete/1")
                    .andExpect {
                        status { isNoContent() }
                    }
                verify(exactly = 1) { studentService.deleteStudent(1L) }
            }
        }

        @Nested
        inner class UpdateStudent {
            @Test
            fun `should return no content when updating a student`() {
                val studentId = 1L
                every { studentService.updateStudent(studentId, data.getStudentRequest(firstName = "new Name")) } just Runs

                mvc.put("/api/students/edit/$studentId") {
                    contentType = MediaType.APPLICATION_JSON
                    content = mapper.writeValueAsString(data.getStudentRequest(firstName = "new Name"))
                }
                    .andExpect {
                        status { isNoContent() }
                    }
            }
        }
    }
