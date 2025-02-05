package com.example.studentcrm.service

import com.example.studentcrm.BaseUnitTest
import com.example.studentcrm.exceptions.EmailInUseException
import com.example.studentcrm.exceptions.StudentNotFoundException
import com.example.studentcrm.repository.entity.StudentEntity
import com.example.studentcrm.repository.repository.StudentRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

internal class StudentServiceTest : BaseUnitTest() {
    private val studentRepository = mockk<StudentRepository>()

    private val cut = StudentService(studentRepository)

    private val validStudent = data.getStudentEntity()

    @Nested
    inner class CreateStudent {
        @Test
        fun `returns student if body StudentRequest is complete and valid`() {
            every { studentRepository.existsByEmail(validStudent.email) } returns false
            every { studentRepository.save(any()) } returns validStudent

            val result = cut.createStudent(data.getStudentRequest())

            result shouldBe validStudent
        }

        @Test
        fun `throws exception if email is already in use`() {
            every { studentRepository.existsByEmail(validStudent.email) } returns true

            val exception = assertThrows<EmailInUseException> { cut.createStudent(data.getStudentRequest()) }

            exception shouldHaveMessage "Email ${validStudent.email} is already in use !"
        }
    }

    @Nested
    inner class FindStudentById {
        @Test
        fun `returns student after giving id`() {
            every { studentRepository.findByIdOrNull(1) } returns validStudent

            val result = cut.findById(validStudent.id)

            result shouldBe validStudent
        }

        @Test
        fun `throws exception when student is not found`() {
            every { studentRepository.findByIdOrNull(validStudent.id) } returns null

            val exception =
                assertThrows<StudentNotFoundException> {
                    cut.findById(validStudent.id)
                }
            exception shouldHaveMessage "Student not found"
        }
    }

    @Nested
    inner class FindAllStudents {
        @Test
        fun `returns all students`() {
            every { studentRepository.findAll() } returns listOf(validStudent)

            val result = cut.findAll()

            result[0] shouldBe validStudent
        }
    }

    @Nested
    inner class DeleteStudent {
        @Test
        fun `when deleting an existing student, ensure it is deleted`() {
            every { studentRepository.findByIdOrNull(validStudent.id) } returns validStudent
            every { studentRepository.delete(validStudent) } just Runs

            cut.deleteStudent(validStudent.id)

            verify(exactly = 1) { studentRepository.delete(validStudent) }
        }
    }

    @Nested
    inner class UpdateStudent {
        private val studentId = validStudent.id

        @Test
        fun `when updating a student ensure it is updated`() {
            val newName = "New Name"
            every { studentRepository.findByIdOrNull(studentId) } returns validStudent
            every { studentRepository.save(any()) } returns data.getStudentEntity(firstName = newName)
            every { studentRepository.existsByEmail(any()) } returns false

            cut.updateStudent(studentId, data.getStudentRequest(newName))

            validStudent.firstName shouldBe newName
        }

        @Test
        fun `when email is already in use throw an exception`() {
            every { studentRepository.findByIdOrNull(studentId) } returns StudentEntity(id = 1, firstName = "firstname", lastName = "lastname", email = "different@email.com")
            every { studentRepository.existsByEmail(data.getStudentRequest().email) } returns true

            val exception =
                assertThrows<EmailInUseException> {
                    cut.updateStudent(studentId, data.getStudentRequest())
                }
            exception shouldHaveMessage "Email ${data.getStudentRequest().email} is already in use !"
        }
    }
}
