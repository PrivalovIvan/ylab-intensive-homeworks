plugins {
    application
    id("java")
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "2.2.2"
}

group = "com.ylab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("com.ylab.homework_1.Homework1Application")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.postgresql:postgresql:42.7.5")

    implementation("org.liquibase:liquibase-core:4.31.0")

    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    testImplementation("org.testcontainers:postgresql:1.19.7")
    testImplementation("org.testcontainers:testcontainers:1.19.7")
    testImplementation("org.mockito:mockito-core:5.16.1")


    liquibaseRuntime("org.liquibase:liquibase-core:4.31.0")
    liquibaseRuntime("org.postgresql:postgresql:42.7.5")
    liquibaseRuntime("info.picocli:picocli:4.7.6")

    implementation("org.mindrot:jbcrypt:0.4")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "changelogFile" to "src/main/resources/db/migration/changelog.xml",
            "url" to "jdbc:postgresql://localhost:5433/finance_db",
            "username" to "finance_user",
            "password" to "finance_password",
        )
    }
    runList = "main"
}

tasks.register("dev") {
    dependsOn("update")
}