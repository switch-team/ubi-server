plugins {
    id("java")
}

group = "org.kloverde"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter:5.6.0")
    implementation(project(":numberutil"))
}

tasks.test {
    useJUnitPlatform()
}