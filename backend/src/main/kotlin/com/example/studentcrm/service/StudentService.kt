package com.example.studentcrm.service

import com.example.studentcrm.dtos.StudentRequestDto
import com.example.studentcrm.exceptions.EmailInUseException
import com.example.studentcrm.exceptions.StudentNotFoundException
import com.example.studentcrm.repository.entity.StudentEntity
import com.example.studentcrm.repository.repository.StudentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StudentService(val studentRepository: StudentRepository) {
    /**
     * create a student if all given data are valid
     * throw EmailInUseException if email is already in use
     * @param studentDTO request body
     * @return created student entity
     */
    fun createStudent(studentDTO: StudentRequestDto): StudentEntity {
        if (emailInUse(studentDTO.email)) {
            throw EmailInUseException("Email ${studentDTO.email} is already in use !")
        }

        val student = StudentEntity(firstName = studentDTO.firstName, lastName = studentDTO.lastName, email = studentDTO.email)
        return studentRepository.save(student)
    }

    /**
     * get a student given his id
     * throws StudentNotFoundException if student does not exist in database
     * @param id student id
     * @return student entity
     */
    fun findById(id: Long) = studentRepository.findByIdOrNull(id) ?: throw StudentNotFoundException()

    /**
     * get all students back
     * throw StudentNotFoundException in case no students exists in database
     * @return list of students
     */
    fun findAll(): List<StudentEntity> {
        val students = studentRepository.findAll()
        return students
    }

    /**
     * delete a student given id
     * @param id student id
     */
    fun deleteStudent(id: Long) {
        val student = findById(id)
        student.courses?.forEach { course -> course.students?.remove(student) }
        studentRepository.delete(student)
    }

    /**
     * update student given id
     * @param id student id
     * @param studentRequest request body
     */
    fun updateStudent(
        id: Long,
        studentRequest: StudentRequestDto,
    ) {
        val student = findById(id)

        // case email is different from the original
        if (student.email != studentRequest.email) {
            if (emailInUse(studentRequest.email)) {
                throw EmailInUseException("Email ${studentRequest.email} is already in use !")
            }

            student.email = studentRequest.email
        }

        student.firstName = studentRequest.firstName
        student.lastName = studentRequest.lastName

        studentRepository.save(student)
    }

    /**
     * helper method to check if email is already in use
     * @param email
     */
    fun emailInUse(email: String) = studentRepository.existsByEmail(email)
}
