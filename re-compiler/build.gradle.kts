plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("tech.medivh.plugin.publisher") version "1.2.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.0.0-1.0.23")
    implementation("androidx.room:room-common:2.6.1")
//    implementation("io.github.sumuve:room-extension-common:${rootProject.extra["localVersion"].toString()}")
    implementation(project(":re-common"))
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.6.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.6.0")
    testImplementation("junit:junit:4.13.2")
}
val Id="room-extension-compiler"
medivhPublisher{
    groupId = "io.github.sumuve"
    artifactId = Id
    version = rootProject.extra["localVersion"].toString()
    pom{
        name = "RoomExtension"
        description = "Android Room扩展库，以Kotlin DSL查询数据库"
        url = "https://github.com/sumuve/RoomExtension"
        licenses{
            license{
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "CJJ"
                name = "CJJ"
                email = "sumuve@qq.com"
            }
        }
        scm {
            connection = "scm:git:"
            url = "https://github.com/sumuve/RoomExtension"
        }
    }
}