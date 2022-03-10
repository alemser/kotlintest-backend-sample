import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

extra["kotlin-coroutines.version"] = "1.6.0"

group = "com.example"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	val kotestVersion = "5.1.0"
	val restAssuredVersion = "4.5.1"

	// basics for a spring boot reactive kotlin project
	implementation ("org.springframework.boot:spring-boot-starter-webflux")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
	implementation ("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation ("org.jetbrains.kotlin:kotlin-reflect")
	implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation ("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.5")

	//database migration tool and driver
	implementation ("org.flywaydb:flyway-core")
	runtimeOnly ("org.postgresql:postgresql")

	//spring boot test support and kotlintest spring extension
	testImplementation ("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("io.kotest.extensions:kotest-extensions-spring:1.1.0")

	//kotlintest and restassured
	testImplementation ("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation ("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation ("io.rest-assured:rest-assured:$restAssuredVersion")
	testImplementation ("io.rest-assured:json-path:$restAssuredVersion")
	testImplementation ("io.rest-assured:xml-path:$restAssuredVersion")
	testImplementation ("io.rest-assured:json-schema-validator:$restAssuredVersion")

	// makes things smoother for Kotlin and Mockito
	testImplementation ("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

	// test container for postgres
	testImplementation ("org.testcontainers:postgresql:1.16.3")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	systemProperty("spring.profiles.active", "local")
	useJUnitPlatform()
}
