// Tell gradle that this is in fact a java project
plugins {
    java
}

// Add maven central as the repository to resolve dependencies
repositories {
    mavenCentral()
}

// Set java version to Java 21
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.13.2")) // Make sure junit versions are compatible
    testImplementation("org.junit.jupiter:junit-jupiter") // JUnit 5 api
    testImplementation("org.assertj:assertj-core:3.27.3") // AssertJ for fluent assertions

    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // JUnit 5 runtime support
}

// Add 'src/' as a source directory
// TODO: Sources should be move to src/main/java
sourceSets.main {
    java {
        srcDirs("src")
    }
    resources {
        srcDirs("resources")
    }
}

// Add 'test/' as a test source directory
// TODO: Tests should be moved to src/test/java
sourceSets.test {
    java {
        srcDirs("test")
    }
}

tasks.test {
    useJUnitPlatform() // Use JUnit 5 for testing
}