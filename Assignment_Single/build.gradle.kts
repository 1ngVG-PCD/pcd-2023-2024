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
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.11.1")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.11.2")
    testImplementation("io.vertx:vertx-junit5:4.5.9")
    testImplementation("com.typesafe.akka:akka-actor-testkit-typed_2.13:2.8.7")

        implementation("org.apache.pdfbox:pdfbox:3.0.3")
        implementation("org.apache.pdfbox:preflight:3.0.2")

        implementation("io.vertx:vertx-core:4.5.3")

        implementation ("io.reactivex.rxjava3:rxjava:3.1.8")

        implementation("com.typesafe.akka:akka-actor-typed_2.13:2.8.7")
}

tasks.test {
    useJUnitPlatform()
}