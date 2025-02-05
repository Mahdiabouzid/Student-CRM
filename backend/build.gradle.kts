import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    // id("io.gitlab.arturbosch.detekt") version "1.23.6"
    //id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("org.asciidoctor.jvm.convert") version "3.3.2" // enables me to convert AsciiDoc files to other formats like HTML or PDF.
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.22"
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val asciidoctorExt by configurations.creating
val snippetsDir = file("build/generated-snippets")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.flywaydb:flyway-core:9.22.3")

    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.1")

    testImplementation("org.assertj:assertj-core:3.6.1")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:kotlin-extensions:4.5.1")
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.22")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured:3.0.1")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.1")
    testImplementation("org.testcontainers:postgresql:1.19.7")
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
}

tasks {
    asciidoctor {
        dependsOn(test) // Generates documentation after running tests
        //baseDirFollowsSourceDir() // Ensures the base directory follows the source directory
        baseDirFollowsSourceFile()
        configurations("asciidoctorExt")
        inputs.dir(snippetsDir)

        // Options for AsciiDoctor
        options(
            mapOf(
                "doctype" to "book",
                "backend" to "html5",
            ),
        )

        // Attributes for AsciiDoctor
        attributes(
            mapOf(
                "snippets" to snippetsDir.absolutePath, // Directory for test-generated snippets
                "source-highlighter" to "coderay", // Syntax highlighting
                "toclevels" to "3", // Table of Contents levels
                "toc" to "left", // Table of Contents on the left
                "sectlinks" to "true", // Section links
                "data-uri" to "true", // Embed assets
                "nofooter" to "true", // No footer
            ),
        )
    }

    asciidoctorj {
        fatalWarnings("include file not found") // Treat missing includes as fatal errors
        modules { diagram.use() } // Enable diagram module
    }

    bootJar {
        dependsOn(asciidoctor) // Ensure doc are generated before creating the jar
        from(asciidoctor) {
            into("BOOT-INF/classes/static/doc") // Copy docs into the jar
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE // Set duplicate handling strategy to include
        manifest {
            attributes["Implementation-Title"] = "student-crm-service"
            attributes["Implementation-Version"] = archiveVersion
        }
        archiveBaseName.set("student-crm-service")
        archiveVersion.set("") // Omit version from jar file name
    }

    // Copy the generated HTML documentation to src/main/resources/static/doc
    register<Copy>("copyDocs") {
        dependsOn(asciidoctor)
        from(asciidoctor) // Get output directly from the task
        include("**/*.html") // include only HTML files
        into("src/main/resources/static/doc")
        //duplicatesStrategy = DuplicatesStrategy.INCLUDE // Set duplicate handling strategy to include
    }

    named("build") {
        dependsOn("copyDocs")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
