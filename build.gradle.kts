import me.modmuss50.mpp.ReleaseType

plugins {
    id("java-library")
    id("idea")
    alias(libs.plugins.moddevgradle)
    alias(libs.plugins.modpublishplugin)
    kotlin("jvm") version "2.0.0"
}

tasks.named<Wrapper>("wrapper").configure {
    distributionType = Wrapper.DistributionType.BIN
}

@Suppress("ConstPropertyName")
object ModInfo {
    const val minecraft_version = "1.21.1"
    const val minecraft_version_range = "[1.21.1,)"
    const val neoforge_version = "21.1.235"
    const val neoforge_version_range = "[21.1,)"
    const val loader_version_range = "[5.9,)"
    const val parchment_minecraft_version = minecraft_version
    const val parchment_mappings_version = "2024.11.17"

    const val mod_id = "examplemod"
    const val mod_name = "Example Mod"
    const val mod_license = "All Rights Reserved"
    const val mod_version = "1.0.0"
    const val mod_group_id = "dev.bluesheep.examplemod"

    const val curseforge_project_id = ""
    const val modrinth_project_id = ""
}

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
    archivesName = ModInfo.mod_id
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
    version = ModInfo.neoforge_version

    parchment {
        mappingsVersion = ModInfo.parchment_mappings_version
        minecraftVersion = ModInfo.parchment_minecraft_version
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
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
        content {
            includeGroup("thedarkcolour")
        }
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    implementation("thedarkcolour:kotlinforforge-neoforge:5.9.0")

    runtimeOnly("maven.modrinth:ferrite-core:x7kQWVju")
    runtimeOnly("maven.modrinth:jei:UJRXzDfp")
}

var generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    var replaceProperties = mapOf(
        "minecraft_version" to ModInfo.minecraft_version,
        "minecraft_version_range" to ModInfo.minecraft_version_range,
        "neoforge_version" to ModInfo.neoforge_version,
        "neoforge_version_range" to ModInfo.neoforge_version_range,
        "loader_version_range" to ModInfo.loader_version_range,
        "mod_id" to ModInfo.mod_id,
        "mod_name" to ModInfo.mod_name,
        "mod_license" to ModInfo.mod_license,
        "mod_version" to modVersion,
    )
    expand(replaceProperties)
    from("src/main/templates")
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
    changelog = file("changelog.md").readText()
    type = parsePublishType(releaseTypeOverride ?: "STABLE")
    modLoaders.add("neoforge")

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY").getOrElse("")
        projectId = ModInfo.curseforge_project_id
        minecraftVersions.add(ModInfo.minecraft_version)
        client = true
        server = true
    }

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN").getOrElse("")
        projectId = ModInfo.modrinth_project_id
        minecraftVersions.add(ModInfo.minecraft_version)
    }
}
