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
    testImplementation("io.vertx:vertx-junit5:4.5.9")

        implementation("org.apache.pdfbox:pdfbox:3.0.2")
        implementation("org.apache.pdfbox:preflight:3.0.2")

        implementation("io.vertx:vertx-core:4.4.1")

        implementation ("io.reactivex.rxjava3:rxjava:3.1.8")
}

tasks.test {
    useJUnitPlatform()
}