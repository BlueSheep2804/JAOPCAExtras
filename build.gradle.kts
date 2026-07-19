import me.modmuss50.mpp.ReleaseType

plugins {
    id("java-library")
    id("idea")
    alias(libs.plugins.moddevgradle)
    alias(libs.plugins.modpublishplugin)
    kotlin("jvm") version "2.0.0"
}

@Suppress("ConstPropertyName")
object ModInfo {
    const val mod_id = "jaopcaextras"
    const val mod_name = "JAOPCA Extras"
    const val mod_license = "MIT"
    const val mod_version = "1.0.0"
    const val mod_group_id = "dev.bluesheep.jaopcaextras"

    const val curseforge_project_id = ""
    const val modrinth_project_id = ""
}

val minecraftVersion = sc.current.version
val forgeVersion = property("forge_version") as String
val platform = if (sc.current.parsed >= "1.21") {
    "neoforge"
} else "forge"

fun getProperty(name: String): String? {
    return if (project.hasProperty(name)) {
        project.property(name).toString().ifEmpty { null }
    } else null
}

val versionOverride = getProperty("version_override")
val releaseTypeOverride = getProperty("release_type_override")
val isSnapshot = getProperty("is_snapshot").toBoolean()

val modVersion = (versionOverride ?: ModInfo.mod_version).let {
    if (isSnapshot) "${it}-SNAPSHOT" else it
}

version = modVersion
group = ModInfo.mod_group_id

base {
    archivesName = "${ModInfo.mod_id}-$minecraftVersion"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)
kotlin.jvmToolchain(21)

tasks.withType<JavaExec>().configureEach {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.JETBRAINS)
    }
}

neoForge {
    version = forgeVersion

    parchment {
        mappingsVersion = property("parchment_mappings_version") as String
        minecraftVersion = property("parchment_minecraft_version") as String
    }

    // accessTransformers = project.files("src/main/resources/META-INF/accesstransformer.cfg")

    runs {
        register("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", ModInfo.mod_id)
        }

        register("client2") {
            client()

            gameDirectory = project.file("run2")
            programArguments.addAll("--username", "Dev2")

            systemProperty("neoforge.enabledGameTestNamespaces", ModInfo.mod_id)
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", ModInfo.mod_id)
        }

        register("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", ModInfo.mod_id)
        }

        register("data") {
            data()

            gameDirectory = project.file("run-data")

            programArguments.addAll(
                "--mod", ModInfo.mod_id,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath)
        }

        configureEach {
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            systemProperty("forge.logging.markers", "REGISTRIES")

            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        register(ModInfo.mod_id) {
            sourceSet(sourceSets["main"])
        }
    }
}

val localRuntime by configurations.creating
configurations.runtimeClasspath {
    extendsFrom(localRuntime)
}

repositories {
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
}

var generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    var replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "forge_version" to forgeVersion,
        "forge_version_range" to project.property("forge_version_range"),
        "loader_version_range" to project.property("loader_version_range"),
        "mod_id" to ModInfo.mod_id,
        "mod_name" to ModInfo.mod_name,
        "mod_license" to ModInfo.mod_license,
        "mod_version" to modVersion,
    )
    expand(replaceProperties)
    from("../../src/main/templates")
    into("build/generated/sources/modMetadata")
}
sourceSets["main"].resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

sourceSets["main"].resources {
    srcDir("src/generated/resources")

    exclude("**/*.bbmodel")
    exclude("**/*.ase", "**/*.aseprite")
    exclude("src/generated/**/.cache")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

fun parsePublishType(name: String): ReleaseType {
    return try {
        ReleaseType.of(name)
    } catch (e: IllegalArgumentException) {
        ReleaseType.STABLE
    }
}

publishMods {
    displayName = "${ModInfo.mod_name} $modVersion"
    file = tasks.jar.get().archiveFile
    changelog = file("../../changelog.md").readText()
    type = parsePublishType(releaseTypeOverride ?: "STABLE")
    modLoaders.add(platform)

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY").getOrElse("")
        projectId = ModInfo.curseforge_project_id
        minecraftVersions.add(minecraftVersion)
        client = true
        server = true
    }

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN").getOrElse("")
        projectId = ModInfo.modrinth_project_id
        minecraftVersions.add(minecraftVersion)
    }
}
