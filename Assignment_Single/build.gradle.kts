plugins {
    id("java")

}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")

        implementation("org.apache.pdfbox:pdfbox:3.0.2")
        implementation("org.apache.pdfbox:preflight:3.0.2")

}

tasks.test {
    useJUnitPlatform()
}