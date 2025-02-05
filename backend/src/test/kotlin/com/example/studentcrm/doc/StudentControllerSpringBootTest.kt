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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class StudentControllerSpringBootTest : BaseSpringBootTest() {

    private val defaultStudentPath = "/api/students"

    @BeforeEach
    fun setUp() {
        studentRepository.deleteAll()
    }

    @Nested
    inner class GetStudentById {
        @Test
        fun `returns student entity with a response status 200`() {
            val student = studentRepository.save(data.getStudentEntity())

            mockMvc
                .perform(get("$defaultStudentPath/{id}", student.id))
                .andExpectAll(
                    status().isOk,
                    content().contentType(APPLICATION_JSON),
                    content().json(mapper.writeValueAsString(student))
                )
                .andDo(
                    document(
                        "get-student",
                        pathParameters(
                            parameterWithName("id").description(
                                "The technical identifier of the student "
                            )
                        ),
                        responseFields(
                            fieldWithPath("id").description("The technical identifier of the student."),
                            fieldWithPath("firstName").description("First name of the student."),
                            fieldWithPath("lastName").description("Last name of the student."),
                            fieldWithPath("email").description("Email of the student."),
                            fieldWithPath("courses").description("List of courses the student is enrolled in."),
                        ),
                    ),
                )
        }
    }

    @Nested
    inner class GetAllStudents {
        @Test
        fun `should return a list of all students back`() {
            val student = studentRepository.save(data.getStudentEntity())

            mockMvc
                .perform(get(defaultStudentPath))
                .andExpectAll(
                    status().isOk,
                    content().contentType(APPLICATION_JSON),
                    content().json(mapper.writeValueAsString(listOf(student)))
                )
                .andDo(
                    document(
                        "get-all-students",
                        responseFields(
                            fieldWithPath("[].id").description("The technical identifier of the student."),
                            fieldWithPath("[].firstName").description("First name of the student."),
                            fieldWithPath("[].lastName").description("Last name of the student."),
                            fieldWithPath("[].email").description("Email of the student."),
                            fieldWithPath("[].courses").description("List of courses the student is enrolled in."),
                        ),
                    ),
                )
        }
    }

    @Nested
    inner class CreateStudent {
        @Test
        fun `should return created student`() {

            mockMvc
                .perform(
                    post("$defaultStudentPath/create")
                        .content(mapper.writeValueAsString(data.getStudentRequest()))
                        .contentType(APPLICATION_JSON)
                )
                .andExpectAll(
                    status().isCreated,
                    content().contentType(APPLICATION_JSON),
                    content().json(mapper.writeValueAsString(data.getStudentEntity(studentRepository.findAll().first().id)))
                ).andDo(
                    document(
                        "create-student",
                        requestFields(
                            fieldWithPath("firstName").description("First name of the new student."),
                            fieldWithPath("lastName").description("Last name of the new student."),
                            fieldWithPath("email").description("Email of the new student."),
                        ),
                        responseFields(
                            fieldWithPath("id").description("The technical identifier of the new student."),
                            fieldWithPath("firstName").description("First name of the new student."),
                            fieldWithPath("lastName").description("Last name of the new student."),
                            fieldWithPath("email").description("Email of the new student."),
                            fieldWithPath("courses").description("List of courses the new student is enrolled in."),
                        )
                    )
                )

            val result = studentRepository.findAll().first()

           result.email shouldBe "test@test.com"
        }
    }

    @Nested
    inner class DeleteStudent {
        @Test
        fun `should delete student`() {
            val student = studentRepository.save(data.getStudentEntity())

            mockMvc
                .perform(
                    delete("$defaultStudentPath/delete/{id}", student.id)
            ).andExpect(
                status().isNoContent
            ).andDo(
                    document(
                        "delete-student",
                        pathParameters(
                            parameterWithName("id").description(
                                "The technical identifier of the Student to be deleted."
                            )
                        )
                    ),
            )

            studentRepository.findById(student.id) shouldBe Optional.empty()
        }
    }

    @Nested
    inner class UpdateStudent {
        @Test
        fun `should update student`() {
            val student = studentRepository.save(data.getStudentEntity())
            val newStudentName = "New Name"

            mockMvc
                .perform(
                    put("$defaultStudentPath/edit/{id}", student.id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(data.getStudentRequest(newStudentName)))
                )
                .andExpect(status().isNoContent)
                .andDo(
                    document(
                        "update-student",
                        pathParameters(
                            parameterWithName("id").description(
                                "The technical identifier of the student to be updated."
                            )
                        ),
                        requestFields(
                            fieldWithPath("firstName").optional().description("The new first name of the student"),
                            fieldWithPath("lastName").optional().description("The new last name of the student"),
                            fieldWithPath("email").optional().description("The new email of the student"),
                        ),
                    )
                )

            studentRepository.findByIdOrNull(student.id)?.firstName shouldBe newStudentName
        }
    }
}
