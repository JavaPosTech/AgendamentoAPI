plugins {
	java
	id("jacoco")
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
}

version = "1.0.0"
group = "br.com.fiap"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

tasks.named<Jar>("jar") {
	enabled = false
}

configurations.configureEach {
	exclude(group = "ch.qos.logback", module = "logback-classic")
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	runtimeOnly("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-database-postgresql")

	implementation("org.slf4j:slf4j-api")
	implementation("org.apache.logging.log4j:log4j-slf4j-impl")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")

	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	testImplementation("org.mockito:mockito-core:5.12.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")

}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.named<JacocoReport>("jacocoTestReport") {
	dependsOn(tasks.test)

	reports {
		html.required.set(true)
	}

	classDirectories.setFrom(
		files(
			classDirectories.files.map {
				fileTree(it) {
					exclude(
						"**/config/**",
						"**/enums/**",
						"**/exceptions/**",
						"**/model/**",
						"**/AgendamentoAPIApplication.class"
					)
				}
			}
		)
	)
}