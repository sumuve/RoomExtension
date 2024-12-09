plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.cjj.re"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
            allVariants()
        }
    }
}

dependencies {
    compileOnly("androidx.room:room-runtime:2.6.1")
    implementation("androidx.annotation:annotation:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
val Id = "room-extension"
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sumuve"
            artifactId = Id
            version = rootProject.extra["localVersion"].toString()
            afterEvaluate {
                from(components["release"])
            }
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
    sign(publishing.publications["release"])
}
tasks.register<Zip>("generateRepo") {
    val publishTask = tasks.named(
        "publishReleasePublicationToMyrepoRepository",
        PublishToMavenRepository::class.java)
    from(publishTask.map { it.repository.url })
    into("mylibrary")
    archiveFileName.set("mylibrary.zip")
}