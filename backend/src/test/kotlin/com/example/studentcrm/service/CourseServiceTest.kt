package com.example.studentcrm.service

import com.example.studentcrm.BaseUnitTest
import com.example.studentcrm.exceptions.CourseExistsException
import com.example.studentcrm.exceptions.CourseNotFoundException
import com.example.studentcrm.exceptions.StudentAlreadyInCourseException
import com.example.studentcrm.exceptions.StudentNotFoundException
import com.example.studentcrm.exceptions.StudentNotInCourseException
import com.example.studentcrm.repository.repository.CourseRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

internal class CourseServiceTest : BaseUnitTest() {
    private val courseRepository: CourseRepository = mockk<CourseRepository>()

    private val studentService: StudentService = mockk<StudentService>()

    private val cut: CourseService = CourseService(courseRepository, studentService)

    private val validCourse = data.getCourseEntity()

    private val validStudent = data.getStudentEntity()

    @Nested
    inner class CreateCourse {

        @Test
        fun `returns course if request is complete and valid`() {
            // arrange
            every { courseRepository.existsByCourseName(data.getCourseRequest().courseName) } returns false
            every { courseRepository.save(any()) } returns validCourse

            // act
            val result = cut.createCourse(data.getCourseRequest())

            // assert
            result shouldBe validCourse
        }

        @Test
        fun `throws CourseExistsException if name is already in use`() {
            every { courseRepository.existsByCourseName(data.getCourseRequest().courseName) } returns true

            val exception =
                assertThrows<CourseExistsException> {
                    cut.createCourse(data.getCourseRequest())
                }

            exception shouldHaveMessage "Course ${data.getCourseRequest().courseName} already exists !"
        }
    }

    @Nested
    inner class FindCourseById {
        private val id = 1L

        @Test
        fun `returns course after given id`() {
            every { courseRepository.findByIdOrNull(id) } returns validCourse

            val result = cut.findCourseById(id)

            result shouldBe validCourse
        }

        @Test
        fun `throws CourseNotFoundException when course is not found`() {
            every { courseRepository.findByIdOrNull(id) } returns null

            val exception =
                assertThrows<CourseNotFoundException> {
                    cut.findCourseById(id)
                }

            exception shouldHaveMessage "Course not found"
        }
    }

    @Nested
    inner class FindAllCourses {
        @Test
        fun `returns all courses`() {
            every { courseRepository.findAll() } returns listOf(validCourse)

            val result = cut.findAllCourses()

            result shouldContainExactlyInAnyOrder listOf(validCourse)
        }

        @Test
        fun `returns an empty list`() {
            every { courseRepository.findAll() } returns listOf()

            val result = cut.findAllCourses()

            result shouldBe emptyList()
        }
    }

    @Nested
    inner class AddStudentToCourse {
        private val courseId = validCourse.id

        private val studentId = validStudent.id

        @Test
        fun `add student to course given both ids`() {
            every { courseRepository.findByIdOrNull(courseId) } returns validCourse
            every { studentService.findById(studentId) } returns validStudent
            every { courseRepository.save(validCourse) } returns validCourse

            val result = cut.addStudentToCourse(courseId, studentId)

            result shouldNotBe null
            result.students!!.size shouldBe 1
            result.students!! shouldContain validStudent
        }

        @Test
        fun `throw StudentNotFoundException if student does not exist`() {
            every { studentService.findById(studentId) } throws StudentNotFoundException()

            val exception = shouldThrow<StudentNotFoundException> { cut.addStudentToCourse(courseId, studentId) }

            exception shouldHaveMessage "Student not found"
        }

        @Test
        fun `throw StudentAlreadyInCourseException when adding a student that is already in course`() {
            every { courseRepository.findByIdOrNull(courseId) } returns validCourse
            every { studentService.findById(studentId) } returns validStudent

            validCourse.students?.add(validStudent)

            val exception =
                assertThrows<StudentAlreadyInCourseException> {
                    cut.addStudentToCourse(courseId, studentId)
                }

            exception shouldHaveMessage "Student already part of this course"
        }
    }

    @Nested
    inner class DeleteCourse {
        private val courseId = validCourse.id

        @Test
        fun `deletes a course given id`() {
            every { courseRepository.findByIdOrNull(courseId) } returns validCourse
            every { courseRepository.delete(validCourse) } just Runs

            cut.deleteCourse(courseId)

            verify { courseRepository.findByIdOrNull(courseId) }
            verify { courseRepository.delete(validCourse) }
        }

        @Test
        fun `throws CourseNotFoundException if course does not exist`() {
            every { courseRepository.findByIdOrNull(courseId) } returns null

            val exception =
                assertThrows<CourseNotFoundException> {
                    cut.deleteCourse(courseId)
                }

            exception shouldHaveMessage "Course not found"
            verify { courseRepository.findByIdOrNull(courseId) }
            verify(exactly = 0) { courseRepository.delete(validCourse) }
        }
    }

    @Nested
    inner class RemoveStudentFromCourse {
        private val studentId = data.getStudentEntity().id

        private val courseId = data.getCourseEntity().id

        @BeforeEach
        fun init() {
            every { studentService.findById(studentId) } returns validStudent
            every { courseRepository.findByIdOrNull(courseId) } returns validCourse
            every { courseRepository.save(validCourse) } returns validCourse
        }

        @Test
        fun `should assert that course has an empty list after deleting student`() {
            validCourse.students!!.add(validStudent)

            val result = cut.removeStudentFromCourse(courseId, studentId)

            result.students!!.isEmpty() shouldBe true
            verify(exactly = 1) { studentService.findById(studentId) }
            verify(exactly = 1) { courseRepository.findByIdOrNull(courseId) }
        }

        @Test
        fun `when deleting a student that is not part of the course throw exception`() {
            val exception =
                assertThrows<StudentNotInCourseException> {
                    cut.removeStudentFromCourse(courseId, studentId)
                }

            exception shouldHaveMessage "Student is not part of this course"
        }

        @Test
        fun `when student does not exist throw an exception`() {
            every { studentService.findById(studentId) } throws StudentNotFoundException()

            val exception =
                assertThrows<StudentNotFoundException> {
                    cut.removeStudentFromCourse(courseId, studentId)
                }

            exception shouldHaveMessage "Student not found"
        }
    }

    @Nested
    inner class UpdateCourse {
        private val courseId = data.getCourseEntity().id

        private val newCourseName = "NewCourse"

        @BeforeEach
        fun init() {
            every { courseRepository.findByIdOrNull(courseId) } returns validCourse
        }

        @Test
        fun `when updating a course ensure it is updated`() {
            every { courseRepository.existsByCourseName(newCourseName) } returns false
            every { courseRepository.save(any()) } returns data.getCourseEntity(courseName = newCourseName)

            // act
            cut.updateCourse(courseId, data.getCourseRequest(newCourseName))

            // assert
            validCourse.courseName shouldBe newCourseName
        }

        @Test
        fun `when giving a name that already exists throw a CourseExistsException`() {
            // arrange
            every { courseRepository.existsByCourseName("NewCourse") } returns true

            // Act & Assert
            val exception =
                assertThrows<CourseExistsException> {
                    cut.updateCourse(courseId, data.getCourseRequest(newCourseName))
                }

            exception shouldHaveMessage "Course with name $newCourseName already exists !"
        }
    }
}
