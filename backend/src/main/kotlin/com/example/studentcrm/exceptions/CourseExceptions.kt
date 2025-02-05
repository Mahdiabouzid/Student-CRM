package com.example.studentcrm.exceptions

class CourseNotFoundException(message: String = "Course not found") : RuntimeException(message)

class CourseExistsException(message: String) : RuntimeException(message)
