package com.example.studentcrm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StudentCrmApplication

fun main(args: Array<String>) {
    runApplication<StudentCrmApplication>(*args)
}
