import java.net.URL

plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	// Depedencies for Ebay hawk
	id ("org.openapi.generator") version "7.0.1"
}

group = "io.irw"
version = "1.0-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

// Call it manually
//tasks.named("compileJava") {
//	dependsOn("generateEbayApiClient")
//}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["springBootAdminVersion"] = "3.1.5"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.assertj:assertj-core:3.24.2")




	implementation("de.codecentric:spring-boot-admin-starter-server")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")
	implementation("org.liquibase:liquibase-core")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.projectlombok:lombok:1.18.30")

	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	// Mapstruct Processor (Annotation Processor)
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	// Ebay hawk dependencies
	// OpenAPI client dependencies
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.16")
	implementation("io.swagger.core.v3:swagger-models:2.2.16")
	implementation("com.ebay.auth:ebay-oauth-java-client:1.1.8")
	implementation("org.jgrapht:jgrapht-core:1.5.2")
}

dependencyManagement {
	imports {
		mavenBom("de.codecentric:spring-boot-admin-dependencies:${property("springBootAdminVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets {
	main {
		java {
			srcDir("src/main/java")
			srcDir("${buildDir}/generated-sources/ebay-buy-browse-api/src/main/java")
			srcDir("${buildDir}/generated-sources/ebay-feed-api/src/main/java")
		}
	}
}

data class ApiSpec(val apiArea: String, val apiName: String, val specUrl: String)

val ebayApiList = listOf(
		ApiSpec("buy", "browse", "https://developer.ebay.com/api-docs/master/buy/browse/openapi/3/buy_browse_v1_oas3.yaml"),
		ApiSpec("buy", "feed", "https://developer.ebay.com/api-docs/master/buy/feed/v1/openapi/3/buy_feed_v1_oas3.yaml"),
		// Add more API specifications as needed
)

tasks.register("downloadEbaySpecs") {
	doLast {
		// Define the destination directory within the project
		val destinationDir = layout.projectDirectory.dir("specs/ebay").asFile

		// Create the destination directory if it doesn't exist
		destinationDir.mkdirs()

		// Iterate over the ebayApiList and download each specification
		ebayApiList.forEach { apiSpec ->
			val specFileName = "${apiSpec.apiArea}_${apiSpec.apiName}_v1_oas3.yaml"
			val specFile = destinationDir.resolve(specFileName)

			val specUrlObject = URL(apiSpec.specUrl)
			specUrlObject.openStream().use { input ->
				specFile.outputStream().use { output ->
					input.copyTo(output)
				}
			}
		}

		println("${ebayApiList.size} eBay specifications downloaded to: $destinationDir")
	}
}

// Define a function to generate tasks
fun createGenerateEbayClientTask(apiSpec: ApiSpec) {
	tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateEbay${apiSpec.apiArea.capitalize()}${apiSpec.apiName.capitalize()}ApiClient") {
		generatorName.set("java")
		library.set("resttemplate") // TODO: Wanted to use webclient, but https://stackoverflow.com/q/76730229/1421254
		inputSpec.set("specs/ebay/${apiSpec.apiArea}_${apiSpec.apiName}_v1_oas3.yaml")
		outputDir.set("${buildDir}/generated-sources/ebay-${apiSpec.apiArea}-${apiSpec.apiName}-api")
		apiPackage.set("com.ebay.${apiSpec.apiArea}.${apiSpec.apiName}.api")
		modelPackage.set("com.ebay.${apiSpec.apiArea}.${apiSpec.apiName}.model")
		invokerPackage.set("com.ebay.${apiSpec.apiArea}.${apiSpec.apiName}.invoker")
		configOptions.set(mapOf(
				"dateLibrary" to "java8",
				// needed because of https://stackoverflow.com/a/74996014
				"useJakartaEe" to "true",
				"generateClientAsBean" to "true",
				"additionalModelTypeAnnotations" to "@lombok.Builder; @lombok.AllArgsConstructor",
//				"webclientBlockingOperations" to "true"
		))
		generateApiTests.set(false)
		generateModelTests.set(false)
	}
}

// Create tasks using the list of ApiSpec objects
ebayApiList.forEach { apiSpec ->
	createGenerateEbayClientTask(apiSpec)
}

// Define a task to generate all API clients
tasks.register("generateAllApiClients") {
	dependsOn("downloadEbaySpecs")
	finalizedBy(ebayApiList.map { apiSpec ->
		tasks.named("generateEbay${apiSpec.apiArea.capitalize()}${apiSpec.apiName.capitalize()}ApiClient")
	})
}