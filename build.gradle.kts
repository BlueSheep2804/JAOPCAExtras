import me.modmuss50.mpp.ReleaseType
import net.neoforged.moddevgradle.dsl.NeoForgeExtension
import net.neoforged.moddevgradle.legacyforge.dsl.LegacyForgeExtension
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    id("java-library")
    id("idea")
    alias(libs.plugins.moddevgradle) apply false
    alias(libs.plugins.moddevgradlelegacy) apply false
    alias(libs.plugins.modpublishplugin)
    alias(libs.plugins.fletchingtable)
    kotlin("jvm") version "2.0.0"
}

stonecutter {
    replacements.string(current.parsed >= "1.21") {
        replace("net.minecraftforge", "net.neoforged.neoforge")
        replace("net.minecraftforge.fml", "net.neoforged.fml")
        replace("net.minecraftforge.eventbus", "net.neoforged.bus")
    }
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

val mcVersion = sc.current.version
val forgeLoaderVersion = property("forge_version") as String
val platform = if (sc.current.parsed >= "1.21") {
    "neoforge"
} else "forge"
val isForge = platform == "forge"
val projectJavaVersion = when {
    sc.current.parsed >= "1.20.5" -> 21
    else -> 17
}

@Suppress("PropertyName")
val useOverlay1_21_1 = sc.current.parsed >= "1.21.1"
@Suppress("PropertyName")
val overlay1_21_1 = rootProject.file("overlay/1.21.1")

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
    archivesName = "${ModInfo.mod_id}-$mcVersion"
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(projectJavaVersion))
    vendor.set(JvmVendorSpec.JETBRAINS)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(projectJavaVersion))
        vendor.set(JvmVendorSpec.JETBRAINS)
    }
}

fletchingTable {
    j52j.register("main") {
        val pattern = "data/${ModInfo.mod_id}/recipes/**/*.json5"
        if (useOverlay1_21_1) {
            extension("json", "$pattern -> ../recipe")
        } else {
            extension("json", pattern)
        }
    }
}

var generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    var replaceProperties = mapOf(
        "minecraft_version" to mcVersion,
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "minecraft_resource_format" to project.property("minecraft_resource_format"),
        "forge_version" to forgeLoaderVersion,
        "forge_version_range" to project.property("forge_version_range"),
        "loader_version_range" to project.property("loader_version_range"),
        "mod_id" to ModInfo.mod_id,
        "mod_name" to ModInfo.mod_name,
        "mod_license" to ModInfo.mod_license,
        "mod_version" to modVersion,
    )
    expand(replaceProperties)
    from("../../src/main/templates")
    if (isForge) {
        exclude("**/neoforge.mods.toml")
    } else {
        exclude("**/mods.toml")
        exclude("**/pack.mcmeta")
    }
    into("build/generated/sources/modMetadata")
}

sourceSets["main"].resources {
    srcDir(generateModMetadata)
    srcDir("src/generated/resources")

    if (useOverlay1_21_1) {
        srcDir(overlay1_21_1)
    }

    exclude("**/*.bbmodel")
    exclude("**/*.ase", "**/*.aseprite")
    exclude("src/generated/**/.cache")
}

tasks.named<ProcessResources>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

val syncIdeaTexturesOverlay = if (useOverlay1_21_1) {
    tasks.register<Sync>("syncIdeaTexturesOverlay") {
        val processResources = tasks.named<ProcessResources>("processResources")
        dependsOn(processResources)
        from(processResources.map { it.destinationDir })
        into(layout.projectDirectory.dir("out/production/resources"))
    }
} else null

if (isForge) {
    pluginManager.apply(libs.plugins.moddevgradlelegacy.get().pluginId)
    extensions.configure<LegacyForgeExtension> {
        version = "$mcVersion-$forgeLoaderVersion"

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

            configureEach {
                jvmArgument("-XX:+AllowEnhancedClassRedefinition")
                systemProperty("forge.logging.markers", "REGISTRIES")

                logLevel = org.slf4j.event.Level.DEBUG

                syncIdeaTexturesOverlay?.let(::taskBefore)
            }
        }

        mods {
            register(ModInfo.mod_id) {
                sourceSet(sourceSets["main"])
            }
        }

        ideSyncTask(generateModMetadata)
    }
} else {
    pluginManager.apply(libs.plugins.moddevgradle.get().pluginId)
    extensions.configure<NeoForgeExtension> {
        version = forgeLoaderVersion

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

            configureEach {
                jvmArgument("-XX:+AllowEnhancedClassRedefinition")
                systemProperty("forge.logging.markers", "REGISTRIES")

                logLevel = org.slf4j.event.Level.DEBUG

                syncIdeaTexturesOverlay?.let(::taskBefore)
            }
        }

        mods {
            register(ModInfo.mod_id) {
                sourceSet(sourceSets["main"])
            }
        }

        ideSyncTask(generateModMetadata)
    }
}

tasks.withType<JavaExec>().configureEach {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(projectJavaVersion))
        vendor.set(JvmVendorSpec.JETBRAINS)
    }
}

repositories {
    maven {
        name = "CurseMaven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev")
        content {
            includeGroup("appeng")
        }
    }
}

fun DependencyHandlerScope.depend(name: String, notation: Any) {
    add(
        if (platform == "forge") {
            "mod${name.uppercaseFirstChar()}"
        } else name,
        notation
    )
}

fun DependencyHandlerScope.modImplementation(notation: Any) {
    depend("implementation", notation)
}
fun DependencyHandlerScope.modRuntimeOnly(notation: Any) {
    depend("runtimeOnly", notation)
}

dependencies {
    modImplementation("curse.maven:jaopca-266936:${property("jaopca_version_id")}")

    if (isForge) {
        modImplementation("appeng:appliedenergistics2-forge:${property("ae2_version")}")
    } else {
        modImplementation("org.appliedenergistics:appliedenergistics2:${property("ae2_version")}")
    }

    modRuntimeOnly("curse.maven:immersive-engineering-231951:${property("immersiveengineering_version_id")}")
    modRuntimeOnly("curse.maven:mekanism-268560:${property("mekanism_version_id")}")
    if (sc.current.version == "1.21.1") modRuntimeOnly("curse.maven:emi-580555:8081408")
    modRuntimeOnly("curse.maven:jei-238222:${property("jei_version_id")}")
}

val localRuntime by configurations.creating
configurations.runtimeClasspath {
    extendsFrom(localRuntime)
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
    if (isForge) {
        file.set(tasks.named<Jar>("reobfJar").get().archiveFile)
    } else {
        file.set(tasks.jar.get().archiveFile)
    }
    changelog = file("../../changelog.md").readText()
    type = parsePublishType(releaseTypeOverride ?: "STABLE")
    modLoaders.add(platform)

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY").getOrElse("")
        projectId = ModInfo.curseforge_project_id
        minecraftVersions.add(mcVersion)
        client = true
        server = true
    }

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN").getOrElse("")
        projectId = ModInfo.modrinth_project_id
        minecraftVersions.add(mcVersion)
    }
}
