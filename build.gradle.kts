plugins {
    java
    id("com.github.johnrengelman.shadow") version ("7.1.2")
    kotlin("jvm") version "1.9.20"
    `maven-publish`
}

group = "com.mcyzj"
version = "1.0.0-t"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.googlecode.json-simple:json-simple:1.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}