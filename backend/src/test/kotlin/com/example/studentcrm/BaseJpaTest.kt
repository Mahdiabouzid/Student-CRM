package com.example.studentcrm

import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class BaseJpaTest {
    protected var data = TestDataFactory

}