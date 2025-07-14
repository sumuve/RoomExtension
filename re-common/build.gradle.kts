plugins {
    id("java-library")
    kotlin("jvm")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}
val id = "room-extension-common"
val reVersion = rootProject.extra["localVersion"].toString()
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.sumuve"
            artifactId = id
            version = reVersion

            from(components["java"])
            pom {
                name = "RoomExtension"
                description = "Android Room扩展库，以Kotlin DSL查询数据库"
                url = "https://github.com/sumuve/RoomExtension"
                licenses {
                    license {
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
    }
    repositories {
        maven {
            name = "myrepo"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}
signing {
    sign(publishing.publications["maven"])
}
tasks.register<Zip>("publishToLocal") {
    group = "publishing"
    val publishTask = tasks.named(
        "publishMavenPublicationToMyrepoRepository",
        PublishToMavenRepository::class.java
    )
    from(publishTask.map { it.repository.url })
    into("")
    archiveFileName.set("${id}-${reVersion}.zip")
}