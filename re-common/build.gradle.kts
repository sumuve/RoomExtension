plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("tech.medivh.plugin.publisher") version "1.2.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

medivhPublisher{
    groupId = "io.github.sumuve"
    artifactId = "room-extension-common"
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