package com.example.studentcrm.controller

import com.example.studentcrm.BaseWebMvcTest
import com.example.studentcrm.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(CourseController::class)
internal class CourseControllerWebMVCTest : BaseWebMvcTest() {
    @MockkBean
    private lateinit var courseService: CourseService

    private val defaultCoursePath = "/api/courses"

    @Nested
    inner class GetCourseById {
        @Test
        fun `should return 200 and course response back with id 1`() {
            every { courseService.findCourseById(data.getCourseEntity().id) } returns data.getCourseEntity()

            mvc.get("$defaultCoursePath/${data.getCourseEntity().id}")
                .andExpect {
                    status { isOk() }
                    content {
                        mapper.writeValueAsString(data.getCourseEntity())
                    }
                }
        }
    }

    @Nested
    inner class CreateCourse {
        @Test
        fun `should return 201 and course with id 1`() {
            every { courseService.createCourse(data.getCourseRequest()) } returns data.getCourseEntity()

            mvc.post("$defaultCoursePath/create") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(data.getCourseRequest())
            }.andExpect {
                status { isCreated() }
                content {
                   mapper.writeValueAsString(data.getCourseEntity())
                }
            }
        }

        @Test
        fun `should return error Invalid Course name Empty name`() {
            mvc.post("$defaultCoursePath/create") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(data.getCourseRequest(courseName = ""))
            }.andExpect {
                status { isBadRequest() }
                content {
                    json(
                        """ 
                        {
                          "errors": ["Invalid Course name: Empty name"]
                        }    
                        """.trimIndent(),
                        strict = true,
                    )
                }
            }
        }

        @Test
        fun `should return error Course name is too long`() {
            mvc.post("/api/courses/create") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(data.getCourseRequest(courseName = "das dad dest dwd whats d bla bla bla bla"))
            }.andExpect {
                status { isBadRequest() }
                content {
                    json(
                        """ 
                        {
                          "errors": ["Course name is too long. Must be of maximum 35 characters"]
                        }    
                        """.trimIndent(),
                        strict = true,
                    )
                }
            }
        }
    }

    @Nested
    inner class DeleteCourse {
        @Test
        fun `should return 204 no content`() {
            val id = 1L
            every { courseService.deleteCourse(id) } just Runs

            mvc.delete("$defaultCoursePath/delete/$id")
                .andExpect {
                    status { isNoContent() }
                }
        }
    }

    @Nested
    inner class AddStudentToCourse {
        @Test
        fun `should add student to course and return no content`() {
            val courseId = 1L
            val studentId = 1L
            every { courseService.addStudentToCourse(courseId, studentId) } returns data.getCourseEntity()

            mvc.put("$defaultCoursePath/addStudent?courseId=$courseId&studentId=$studentId")
                .andExpect {
                    status { isNoContent() }
                }
        }
    }

    @Nested
    inner class RemoveStudentFromCourse {
        @Test
        fun `should return no content when removing student from a course`() {
            val courseId = 1L
            val studentId = 1L
            every { courseService.removeStudentFromCourse(courseId, studentId) } returns data.getCourseEntity()

            mvc.put("$defaultCoursePath/removeStudent?courseId=$courseId&studentId=$studentId")
                .andExpect {
                    status { isNoContent() }
                }
        }
    }

    @Nested
    inner class UpdateCourse {
        @Test
        fun `should return no content when updating a course`() {
            val courseId = data.getCourseEntity().id
            every { courseService.updateCourse(courseId, data.getCourseRequest(courseName = "newCourse")) } just Runs

            mvc.put("$defaultCoursePath/edit/$courseId") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(data.getCourseRequest(courseName = "newCourse"))
            }
                .andExpect {
                    status { isNoContent() }
                }
        }
    }
}
