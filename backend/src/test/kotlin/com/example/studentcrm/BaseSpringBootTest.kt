package com.example.studentcrm

import com.example.studentcrm.repository.repository.CourseRepository
import com.example.studentcrm.repository.repository.StudentRepository
import com.fasterxml.jackson.databind.ObjectMapper
import java.text.SimpleDateFormat
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Rollback(false)
abstract class BaseSpringBootTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var courseRepository: CourseRepository

    @Autowired
    protected lateinit var studentRepository: StudentRepository

    protected var data = TestDataFactory

    protected val mapper =
        ObjectMapper().apply {
            findAndRegisterModules()
            dateFormat = SimpleDateFormat("yyyy-MM-dd")
        }
}
