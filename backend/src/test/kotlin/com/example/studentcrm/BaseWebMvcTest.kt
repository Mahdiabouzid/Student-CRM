package com.example.studentcrm

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
abstract class BaseWebMvcTest {
    @Autowired
    protected lateinit var mapper: ObjectMapper

    @Autowired
    protected lateinit var mvc: MockMvc

    protected var data = TestDataFactory
}