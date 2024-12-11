plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")


    // PostgreSQL and Flyway
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // OpenFeign for microservices
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Lombok for boilerplate code reduction
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured:5.3.0")
    testImplementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
    testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.0")
    implementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.0")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.1.2")
    implementation("org.springframework.kafka:spring-kafka:3.3.0")

    testImplementation("org.springframework.kafka:spring-kafka-test:3.3.0")

    testImplementation("org.testcontainers:kafka:1.20.4")

    // SpringDoc for OpenAPI/Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
