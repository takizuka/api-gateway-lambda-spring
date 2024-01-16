import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.github.johnrengelman.shadow") version "8.1.1"
	id("org.springframework.boot.experimental.thin-launcher") version "1.0.31.RELEASE"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
}

group = "com.takizuka"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2023.0.0"

tasks.assemble {
	dependsOn("thinJar", "shadowJar")
}

tasks.withType<ShadowJar> {
	mustRunAfter("thinJar")
	archiveClassifier = "aws"
	manifest.inheritFrom(project.tasks.thinJar.get().manifest)
	// Required for Spring
	mergeServiceFiles()
	append("META-INF/spring.handlers")
	append("META-INF/spring.schemas")
	append("META-INF/spring.tooling")
	append("META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
	append("META-INF/spring/org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration.imports")
	transform(PropertiesFileTransformer::class.java) {
		paths = listOf("META-INF/spring.factories")
		mergeStrategy = "append"
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-function-context")
	implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
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
