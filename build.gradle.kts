import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	id("jacoco")
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.serialization") version "1.8.22"
}

group = "com.currency"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2022.0.3"

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation ("jakarta.validation:jakarta.validation-api")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("io.github.openfeign:feign-core:12.3")
	implementation("io.github.openfeign:feign-gson:12.3")
	implementation("io.github.openfeign:feign-httpclient:12.3")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

	implementation("com.google.code.gson:gson:2.10.1")

	implementation("org.junit.platform:junit-platform-launcher:1.8.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-core:5.4.0")
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")

	testImplementation("com.github.tomakehurst:wiremock:3.0.0-beta-10")
	testImplementation("com.github.tomakehurst:wiremock-standalone:3.0.0-beta-10")
	testImplementation("ru.lanwen.wiremock:wiremock-junit5:1.3.1")
	testImplementation("org.eclipse.jetty:jetty-alpn-openjdk8-server:9.4.43.v20210629")
	testImplementation("org.eclipse.jetty:jetty-alpn-openjdk8-client:9.4.43.v20210629")

}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jacoco {
	toolVersion = "0.8.8"
	reportsDir = file("$buildDir/reports/jacoco")
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(false)
		csv.required.set(false)
		csv.required.set(true)
	}

	finalizedBy("jacocoTestReport")
}