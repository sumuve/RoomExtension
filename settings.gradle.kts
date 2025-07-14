pluginManagement {
    repositories {
        maven { setUrl("https://maven.aliyun.com/nexus/content/groups/public/") }
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://maven.aliyun.com/nexus/content/repositories/jcenter") }
//        maven { url "https://jitpack.io" }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { setUrl("https://maven.aliyun.com/nexus/content/groups/public/") }
        google()
        mavenCentral()
        maven { setUrl("https://maven.aliyun.com/nexus/content/repositories/jcenter") }
//        maven { url "https://jitpack.io" }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}

rootProject.name = "RoomExtension"
include(":app")
include(":re")
include(":re-compiler")
include(":re-common")
