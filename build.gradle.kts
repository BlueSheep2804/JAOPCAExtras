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
}

stonecutter {
    replacements.string(current.parsed >= "1.21") {
        replace("net.minecraftforge", "net.neoforged.neoforge")
        replace("net.minecraftforge.fml", "net.neoforged.fml")
        replace("net.minecraftforge.eventbus", "net.neoforged.bus")
    }

    replacements.string(current.parsed >= "1.21.11") {
        replace("ResourceLocation", "Identifier")
        replace("IdentifierWrapper", "IdentifierWrapper")
        replace("ItemStack", "ItemStackTemplate")
    }

    swaps["ae2_recipe_ingredient"] = when {
        current.parsed >= "26.1" -> "\"$1\": \"$2\"$3"
        else -> "\"$1\": { \"item\": \"$2\" }$3"
    }
}

@Suppress("ConstPropertyName")
object ModInfo {
    const val mod_id = "jaopcaextras"
    const val mod_name = "JAOPCA Extras"
    const val mod_license = "MIT"
    const val mod_version = "5.0.0"
    const val mod_group_id = "dev.bluesheep.jaopcaextras"

    const val curseforge_project_id = "679287"
    const val modrinth_project_id = "lev3YGBh"
}

val mcVersion = sc.current.version
val forgeLoaderVersion = property("forge_version") as String
val platform = if (sc.current.parsed >= "1.21") {
    "neoforge"
} else "forge"
val isForge = platform == "forge"
val projectJavaVersion = when {
    sc.current.parsed >= "26.1" -> 25
    sc.current.parsed >= "1.20.5" -> 21
    else -> 17
}

@Suppress("PropertyName")
val useOverlay1_21_1 = sc.current.parsed >= "1.21.1"
@Suppress("PropertyName")
val overlay1_21_1 = rootProject.file("overlay/1.21.1")

@Suppress("PropertyName")
val useOverlay26_1_2 = sc.current.parsed >= "26.1.2"
@Suppress("PropertyName")
val overlay26_1_2 = rootProject.file("overlay/26.1.2")

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
    if (useOverlay26_1_2) {
        srcDir(overlay26_1_2)
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
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroup("software.bernie.geckolib")
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

fun DependencyHandlerScope.modImplementationIfAvailable(notation: String, version: String?) {
    if (version == null) return
    modImplementation("$notation:$version")
}
fun DependencyHandlerScope.modRuntimeOnlyIfAvailable(notation: String, version: String?) {
    if (version == null) return
    modRuntimeOnly("$notation:$version")
}

dependencies {
    modImplementation("curse.maven:jaopca-266936:${property("jaopca_version_id")}")

    if (isForge) {
        modImplementation("appeng:appliedenergistics2-forge:${property("ae2_version")}")
    } else {
        modImplementation("org.appliedenergistics:appliedenergistics2:${property("ae2_version")}")
    }

    modImplementationIfAvailable("curse.maven:glodium-957920", getProperty("glodium_version_id"))
    modImplementationIfAvailable("curse.maven:ex-pattern-provider-892005", getProperty("extendedae_version_id"))

    val version = sc.current.version
    when (version) {
        "1.20.1" -> {
            modImplementation("curse.maven:advancedae-1084104:6205290")
        }
        else -> {}
    }

    modRuntimeOnlyIfAvailable("curse.maven:immersive-engineering-231951", getProperty("immersiveengineering_version_id"))
    modRuntimeOnlyIfAvailable("curse.maven:mekanism-268560", getProperty("mekanism_version_id"))
    modRuntimeOnlyIfAvailable("curse.maven:energized-power-782147", getProperty("energizedpower_version_id"))
    modRuntimeOnlyIfAvailable("curse.maven:emi-580555", getProperty("emi_version_id"))
    modRuntimeOnlyIfAvailable("curse.maven:jei-238222", getProperty("jei_version_id"))
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
