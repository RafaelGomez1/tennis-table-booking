import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    id("com.diffplug.spotless") version "6.18.0"
    id("com.google.cloud.tools.jib") version "3.4.3"
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("idea")
    kotlin("plugin.spring") version "1.9.0"
    id("java-test-fixtures")
    application
    jacoco
}

group = "com.codely"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

sourceSets {
    create("test-integration") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val testIntegrationImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

configurations["testIntegrationRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets["test-integration"].output.classesDirs
    classpath = sourceSets["test-integration"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter("test")
}

tasks.withType<Test> {
    testLogging {
        showStandardStreams = false
        events("skipped", "failed")
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = TestExceptionFormat.FULL
        afterSuite(printTestResult)
    }
    useJUnitPlatform()
}

val printTestResult: KotlinClosure2<TestDescriptor, TestResult, Void>
    get() = KotlinClosure2({ desc, result ->

        if (desc.parent == null) { // will match the outermost suite
            println("------")
            println(
                "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} " +
                        "successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
            )
            println(
                "Tests took: ${result.endTime - result.startTime} ms."
            )
            println("------")
        }
        null
    })

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    dependsOn(tasks.test)
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging:1.7.8")

    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.9.0")

    // Reactive spring boot
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")

    // KotlinX
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    // Password Encryption
    implementation("org.mindrot:jbcrypt:0.4")

    // Functional Programming
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.1.2")

    // PDF to TEXT
    implementation("org.apache.pdfbox:pdfbox:2.0.7")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test:2.7.2")

    testImplementation("io.kotest:kotest-runner-junit5:5.7.0")
    testImplementation("io.kotest:kotest-assertions-core:5.7.0")
    testImplementation("io.kotest:kotest-property:5.7.0")

    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.3.0") {
        because("provides good testing for arrow")
    }

    testImplementation("in.rcard:assertj-arrow-core:0.2.0")

    // Coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // test fixtures dependencies
    testFixturesImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xcontext-receivers",
            "-Xms256m",
            "-Xmx256m",
            "-Xss256k",
            "-XX:MaxMetaspaceSize=128m",
            "-XX:+UseG1GC",                             // Default GC: G1GC
            "-XX:MaxGCPauseMillis=200",                 // Reduce pause times during GC
            "-XX:+ExplicitGCInvokesConcurrent",         // Enable concurrent explicit GC
            "-XX:MaxRAMPercentage=50.0",                // Limit JVM heap to 50% of available memory
            "-XX:+UseContainerSupport",                 // Enable container awareness
            "-XX:InitialRAMPercentage=25.0",            // Start with 25% of available memory
            "-XX:MaxMetaspaceSize=128m"                 // Limit metaspace memory usage
        )
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    kotlin {
        ktlint().setEditorConfigPath("$rootDir/.editorconfig")
    }
    kotlinGradle {
        ktlint().setEditorConfigPath("$rootDir/.editorconfig")
    }
}

tasks.check {
    dependsOn(integrationTest)
    dependsOn(tasks.spotlessCheck)
}
