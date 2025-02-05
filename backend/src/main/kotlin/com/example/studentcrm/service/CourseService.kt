package com.example.studentcrm.service

import com.example.studentcrm.dtos.CourseRequestDto
import com.example.studentcrm.exceptions.CourseExistsException
import com.example.studentcrm.exceptions.CourseNotFoundException
import com.example.studentcrm.exceptions.StudentAlreadyInCourseException
import com.example.studentcrm.exceptions.StudentNotInCourseException
import com.example.studentcrm.repository.entity.CourseEntity
import com.example.studentcrm.repository.repository.CourseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CourseService(private val courseRepository: CourseRepository, private val studentService: StudentService) {
    /**
     * create a new course if all given data are valid
     * throw CourseExistsException if course already exists
     * @param courseDTO request body
     * @return created course entity
     */
    fun createCourse(courseDTO: CourseRequestDto): CourseEntity {
        if (courseRepository.existsByCourseName(courseDTO.courseName)) {
            throw CourseExistsException("Course ${courseDTO.courseName} already exists !")
        }

        val course = CourseEntity(courseName = courseDTO.courseName)
        return courseRepository.save(course)
    }

    /**
     * find a single course given its id
     * throws CourseNotFoundException if no course found
     * @param id course id
     * @return course entity
     */
    fun findCourseById(id: Long) = courseRepository.findByIdOrNull(id) ?: throw CourseNotFoundException()

    /**
     * find all courses
     */
    fun findAllCourses(): List<CourseEntity> = courseRepository.findAll()

    /**
     * add student to a course
     * @param courseId course id
     * @param studentId student id
     * @return updated course entity
     */
    fun addStudentToCourse(
        courseId: Long,
        studentId: Long,
    ): CourseEntity {
        val student = studentService.findById(studentId)
        val course = findCourseById(courseId)

        if (course.students?.contains(student) == true) {
            throw StudentAlreadyInCourseException()
        }

        course.students?.add(student)
        return courseRepository.save(course)
    }

    fun removeStudentFromCourse(
        courseId: Long,
        studentId: Long,
    ): CourseEntity {
        val student = studentService.findById(studentId)
        val course = findCourseById(courseId)

        if (course.students?.contains(student) == false) {
            throw StudentNotInCourseException()
        }

        course.students?.remove(student)
        return courseRepository.save(course)
    }

    fun deleteCourse(
        id: Long
    ): Unit =
       findCourseById(id).let {
            courseRepository.delete(it)
        }

    fun updateCourse(
        courseId: Long,
        courseDTO: CourseRequestDto,
    ) {
        val course = findCourseById(courseId).apply {
            if (courseRepository.existsByCourseName(courseDTO.courseName)) {
                throw CourseExistsException("Course with name ${courseDTO.courseName} already exists !")
            }
            this.courseName = courseDTO.courseName
        }

        courseRepository.save(course)
    }
}
