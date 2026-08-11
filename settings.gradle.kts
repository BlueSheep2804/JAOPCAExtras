pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "KikuGie Snapshots"
            url = uri("https://maven.kikugie.dev/snapshots")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.6"
}

stonecutter {
    create(rootProject) {
        versions("1.20.1", "1.21.1", "26.1.2")

        vcsVersion = "1.21.1"
    }
}
