package com.example.studentcrm.exceptions

class StudentNotFoundException(message: String = "Student not found") : RuntimeException(message)

class EmailInUseException(message: String) : RuntimeException(message)

class StudentAlreadyInCourseException(message: String = "Student already part of this course") : RuntimeException(message)

class StudentNotInCourseException(message: String = "Student is not part of this course") : RuntimeException(message)
