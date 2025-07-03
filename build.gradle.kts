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