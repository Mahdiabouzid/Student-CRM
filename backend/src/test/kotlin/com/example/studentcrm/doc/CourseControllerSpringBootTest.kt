package com.example.studentcrm.doc

import com.example.studentcrm.BaseSpringBootTest
import io.kotest.matchers.shouldBe
import java.util.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


internal class CourseControllerSpringBootTest: BaseSpringBootTest() {

    private val defaultCoursePath = "/api/courses"

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        studentRepository.deleteAll()
    }

    @Nested
    inner class GetCourseById {
        @Test
        fun `should return a course given its id`() {
            val expectedEntity = courseRepository.save(data.getCourseEntity())
            courseRepository.findById(expectedEntity.id) shouldBe Optional.of(expectedEntity)

            mockMvc
                .perform(get("$defaultCoursePath/{id}", expectedEntity.id))
                .andExpectAll(
                    status().isOk,
                    content().contentType(APPLICATION_JSON),
                    content().json(mapper.writeValueAsString(expectedEntity))
                ).andDo(
                    document(
                        "get-course",
                        pathParameters(
                            parameterWithName("id").description(
                                "The technical identifier of the course "
                            )
                        ),
                        responseFields(
                            fieldWithPath("id").description("The technical identifier of the course."),
                            fieldWithPath("courseName").description("Name of the course."),
                            fieldWithPath("students").description("List of students enrolled in this course."),
                        ),
                    ),
                )
        }

        @Nested
        inner class GetAllCourses {
            @Test
            fun `should return a list of all courses back`() {
                val expectedEntity = courseRepository.save(data.getCourseEntity())
                courseRepository.findById(expectedEntity.id) shouldBe Optional.of(expectedEntity)

                mockMvc
                    .perform(get(defaultCoursePath))
                    .andExpectAll(
                        status().isOk,
                        content().contentType(APPLICATION_JSON),
                        content().json(mapper.writeValueAsString(listOf(expectedEntity)))
                    ).andDo(
                        document(
                            "get-all-courses",
                            responseFields(
                                fieldWithPath("[].id").description("The technical identifier of the course."),
                                fieldWithPath("[].courseName").description("Name of the course."),
                                fieldWithPath("[].students").description("List of students enrolled in this course."),
                            ),
                        ),
                    )
            }
        }

        @Nested
        inner class CreateCourse {
            @Test
            fun `should return created course`() {

                mockMvc
                    .perform(
                        post("$defaultCoursePath/create")
                            .content(mapper.writeValueAsString(data.getCourseRequest()))
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpectAll(
                        status().isCreated,
                        content().contentType(APPLICATION_JSON),
                        content().json(
                            mapper.writeValueAsString(
                                data.getCourseEntity(
                                    courseRepository.findAll().first().id
                                )
                            )
                        )
                    ).andDo(
                        document(
                            "create-course",
                            requestFields(
                                fieldWithPath("courseName").description("Name of the new course"),
                            ),
                            responseFields(
                                fieldWithPath("id").description("The technical identifier of the new course."),
                                fieldWithPath("courseName").description("The name of the new course."),
                                fieldWithPath("students").description("List of students enrolled in the course.")
                            )
                        )
                    )

                val result = courseRepository.findAll().first()

                result?.courseName shouldBe data.getCourseRequest().courseName
            }

        }
    }

    @Nested
    inner class DeleteCourse {
        @Test
        fun `should delete course and returns no content`() {
            val courseToBeDeleted = courseRepository.save(data.getCourseEntity())

            mockMvc
                .perform(
                    delete("$defaultCoursePath/delete/{id}", courseToBeDeleted.id)
                )
                .andExpect(status().isNoContent)
                .andDo(
                    document(
                        "delete-course",
                        pathParameters(
                            parameterWithName("id").description(
                                "The technical identifier of the course to be deleted."
                            )
                        )
                    ),
                )
            courseRepository.findById(courseToBeDeleted.id) shouldBe Optional.empty()
        }
    }

    @Nested
    inner class AddStudentToCourse {
        @Test
        fun `should add student to course and return no content`() {
            val course = courseRepository.save(data.getCourseEntity())
            val student = studentRepository.save(data.getStudentEntity())

            mockMvc
                .perform(
                    put(
                        "$defaultCoursePath/addStudent?courseId={courseId}&studentId={studentId}",
                        course.id,
                        student.id
                    )
                )
                .andExpect(
                    status().isNoContent
                )
                .andDo(
                    document(
                        "add-student-to-course",
                        queryParameters(
                            parameterWithName("courseId").description(
                                "The technical identifier of the course."
                            ),
                            parameterWithName("studentId").description(
                                "The technical identifier of the student."
                            ),
                        )
                    ),
                )
        }
    }

    @Nested
    inner class RemoveStudentFromCourse {
        @Test
        fun `should remove student from course and return no content`() {
            val course = courseRepository.save(data.getCourseEntity())
            val student = studentRepository.save(data.getStudentEntity())

            mockMvc
                .perform(
                    put(
                        "$defaultCoursePath/addStudent?courseId={courseId}&studentId={studentId}",
                        course.id,
                        student.id
                    )
                )
                .andExpect(
                    status().isNoContent
                )
                .andDo(
                    document(
                        "remove-student-from-course",
                        queryParameters(
                            parameterWithName("courseId").description(
                                "The technical identifier of the course."
                            ),
                            parameterWithName("studentId").description(
                                "The technical identifier of the student."
                            ),
                        )
                    ),
                )
        }
    }

    @Nested
    inner class UpdateCourse {
        @Test
        fun `should update course and return no content`() {
           val course = courseRepository.save(data.getCourseEntity())
            val newCourseName = "New Course"

            mockMvc
                .perform(
                    put("$defaultCoursePath/edit/{id}", course.id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(data.getCourseRequest(newCourseName)))
                        )
                .andExpect(status().isNoContent)
                .andDo(
                    document(
                        "update-course",
                        pathParameters(
                            parameterWithName("id").description(
                                "The technical identifier of the course to be updated."
                            )
                        ),
                        requestFields(
                            fieldWithPath("courseName").description("The new name of the course"),
                        ),
                    )
                )

            courseRepository.findByIdOrNull(course.id)?.courseName shouldBe newCourseName
        }
    }
}