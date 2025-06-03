plugins {
    id("org.jetbrains.intellij") version "1.17.0"
    kotlin("jvm") version "2.1.10"
}

group = "com.shashank.todo"
version = "1.6.6"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

intellij {
    version.set("2024.3")
    type.set("IC")
}

tasks {
    patchPluginXml {
        version.set("1.6.6")
        sinceBuild.set("243")
        untilBuild.set("243.*")
    }
}