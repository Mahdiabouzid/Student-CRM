# Student-CRM

## Overview

The Student Customer Relationship Management (CRM) Service is a learning project designed to familiarize developers with the NOVATEC tech stack. The project focuses on tooling, frameworks, and development processes rather than just programming concepts.

## Project Description

The goal of this project is to reduce paperwork at universities by providing a system to manage student and course data. The system will allow administrative users to perform CRUD (Create, Read, Update, Delete) operations on student and course records.

## Core Features

* Manage student and course data.

* A student can enroll in multiple courses.

* A course can have multiple students.

* CRUD functionality for both entities.

* Dockerized environment.

* Database and configurations managed independently.

* Ensuring high code quality with code analysis, formatting rules, and testing.

## Entity Definitions

### Student

* ID: Numeric database sequence.

* Firstname: Text (max 20 characters).

* Lastname: Text (max 20 characters).

* Email: Unique, valid email format.

* Courses: List of enrolled courses (lazy loaded).

### Course

* ID: Numeric database sequence.

* Coursename: Text (max 35 characters).

* Students: List of enrolled students (lazy loaded).

## Tech Stack

### Backend

* Programming Language: Kotlin

* Application Framework: Spring Boot

* Architecture: MVC

* Database (Dockerized): PostgreSQL with Flyway and Spring JPA

* Containerization: Docker (backend, frontend, database)

* Build Tool: Gradle

* Testing:

    * JUnit 5 + MockK (+ AssertK)

    * MockMvc for integration testing

    * Testcontainers for data tests

* Documentation: ASCII Doctor (HTML files published to /doc when running)

### Frontend

* Base Framework: Next.js

* Testing: Jest for component testing

* Styling: Material-UI

* Tailwindcss